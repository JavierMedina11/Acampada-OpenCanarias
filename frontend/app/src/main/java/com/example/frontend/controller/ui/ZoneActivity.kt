package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.util.PreferenceHelper
import com.example.frontend.controller.util.PreferenceHelper.set
import com.example.frontend.controller.util.ZoneAdapter
import com.google.zxing.integration.android.IntentIntegrator
import com.opencanarias.pruebasync.util.AppDatabase
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_zone.*
import kotlinx.android.synthetic.main.another_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ZoneActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private lateinit var zones: ArrayList<Zone>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewPager: ViewPager2
    private lateinit var viewAdapter: ZoneAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    //private lateinit var binding: ActivityZoneBinding
    val MIN_SCALE = 0.85f
    val MIN_ALPHA = 0.5f

    @SuppressLint("WrongConstant")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //binding = ActivityZoneBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_zone)
        //setContentView(binding.root)
        //binding.buttonToQr.setOnClickListener { initScanner() }

        listText.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")
        zones = ArrayList<Zone>()

        var listaZones = emptyList<Zone>()

        zones = ArrayList<Zone>()
        val database = AppDatabase.getDatabase(this)

        database.zonas().getAll().observe(this, Observer {
            listaZones = it as ArrayList<Zone>

            val adapter = ZoneAdapter(listaZones as ArrayList<Zone>, this)
            val view_pager: ViewPager2 = findViewById(R.id.view_pager)
            viewPager = findViewById<ViewPager2>(R.id.view_pager)
            viewPager.adapter = adapter
            view_pager.setClipToPadding(false)
            view_pager.setClipChildren(false)
            view_pager.setOffscreenPageLimit(3)
            view_pager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)
            view_pager.setPadding(125, 0, 125, 0)
            view_pager.setPageTransformer { page, position ->
                page.apply {
                    when {
                        position < -1 -> {
                            alpha = 0f
                            translationY = 35f
                        }
                        position <= 1 -> {
                            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                            scaleX = scaleFactor
                            scaleY = scaleFactor

                            alpha =
                                (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        })

        val num : Int = this.intent.getIntExtra("num", 0)

        val tokenGE : String? = this.intent.getStringExtra("api_token")
        val opeIdGE : String = this.intent.getStringExtra("opeId").toString()

        if (num==1){
            createSessionPreference(tokenGE.toString(), opeIdGE.toInt())
        }

        listeners()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            checkConnection()
        }
        blured()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkConnection(){
        var isNetworkConnected = false
        val context: Context
        val wifi = DownloadManager.Request.NETWORK_WIFI
        val networkMobile = DownloadManager.Request.NETWORK_MOBILE

        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val builder = NetworkRequest.Builder()

        try {
            connectivityManager.registerDefaultNetworkCallback(object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isNetworkConnected = true
                    Log.v("Wifi", wifi.toString())
                    syncDBServerToDBLocalPersons()
                    syncDBServerToDBLocalZones()
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false
                    Log.v("networkMobile", networkMobile.toString())
                }
            });
            isNetworkConnected = false
        }catch (e: Exception){
            isNetworkConnected = false;
        }
    }

    private fun syncDBServerToDBLocalPersons() {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getAllPerson(this) { response ->
            run {
                val database = AppDatabase.getDatabase(this)
                CoroutineScope(Dispatchers.IO).launch{
                    database.personas().delete()
                    val reservaArray : ArrayList<Persona>? = response
                    if (reservaArray != null) {
                        for (i in 0 until reservaArray.size) {
                            database.personas().insert(reservaArray[i])
                        }
                    }
                }
            }
        }
    }

    private fun syncDBServerToDBLocalZones() {
        val tokenPref = preferences.getString("tokenPref", null)
        val zoneImpl = ServiceImpl()
        zoneImpl.getAll(this, tokenPref.toString()) { response ->
            run {
                val database = AppDatabase.getDatabase(this)
                CoroutineScope(Dispatchers.IO).launch{
                    database.zonas().delete()
                    val zoneArray : ArrayList<Zone>? = response
                    if (zoneArray != null) {
                        for (i in 0 until zoneArray.size) {
                            database.zonas().insert(zoneArray[i])
                        }
                    }
                }
            }
        }
    }

    private fun initScanner() {
        Log.v("qr", "dentro del init")
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    private fun listeners() {
        buttonToHelp.setOnClickListener {
                val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.another_view, null)
                val popupWindow = PopupWindow(
                    view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )

                popupWindow.setFocusable(true)
                popupWindow.update()
                blurView.setAlpha(1f)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    popupWindow.elevation = 10.0F
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    val slideIn = Slide()
                    slideIn.slideEdge = Gravity.TOP
                    popupWindow.enterTransition = slideIn

                    val slideOut = Slide()
                    slideOut.slideEdge = Gravity.RIGHT
                    popupWindow.exitTransition = slideOut
                }

                val tv = view.findViewById<TextView>(R.id.text_viewDniOLocalizador)
                val buttonPopup = view.findViewById<ImageButton>(R.id.button_popup)
                val buttonPopup2 = view.findViewById<ImageButton>(R.id.buttonClosePopUpS)
                tv.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")

                buttonPopup.setOnClickListener{
                    var getReservasByLocalizador = emptyList<Reserva>()
                    val localizador = view.findViewById<TextView>(R.id.editTextPutoAmo).text.toString()

                    val database = AppDatabase.getDatabase(this)
                    val preferences = PreferenceHelper.defaultPrefs(this)

                    tv.setText(localizador)
                    database.reservas().getByLocalizador(localizador).observe(this, Observer {
                        getReservasByLocalizador = it
                        Log.v("PRUEBAAAAAAA", "Prueba: " + getReservasByLocalizador[0].id)
                        preferences["reservaSearchId"] = getReservasByLocalizador[0].id
                        val intent = Intent(this, ReservaDetalladaActivity::class.java)
                        startActivity(intent)
                    })
                    blurView.setAlpha(0f)
                }

                buttonPopup2.setOnClickListener{
                    popupWindow.dismiss()
                    Toast.makeText(applicationContext, "Popup closed", Toast.LENGTH_SHORT).show()
                }

                popupWindow.setOnDismissListener {
                    blurView.setAlpha(0f)
                    Toast.makeText(applicationContext, "Popup closed", Toast.LENGTH_SHORT).show()
                }

                TransitionManager.beginDelayedTransition(root_layout)
                popupWindow.showAtLocation(root_layout, Gravity.CENTER, 0, 0)
        }

        avatarProfile2.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun blured(){
        val radius = 21f

        val decorView: View = window.decorView
        val windowBackground: Drawable = decorView.getBackground()

        blurView.setupWith(decorView.findViewById(android.R.id.content))
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true) // Or false if it's in a scrolling container or might be animated
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "cancelado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    this,
                    "el valor escaneado es: ${result.contents}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun createSessionPreference(tokenPref: String, opeId: Int) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["tokenPref"] = tokenPref
        preferences["opeId"] = opeId
    }


}




