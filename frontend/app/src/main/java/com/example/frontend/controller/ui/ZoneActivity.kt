package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.io.ServiceSingleton
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.util.PreferenceHelper
import com.example.frontend.controller.util.PreferenceHelper.set
import com.example.frontend.controller.util.ZoneAdapter
import com.google.zxing.integration.android.IntentIntegrator
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.activity_zone.*
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
        Log.v("Num: ", num.toString())
        val tokenGE : String? = this.intent.getStringExtra("api_token")
        val opeIdGE : String = this.intent.getIntExtra("opeId", 0).toString()
        Log.v("ZoneActi GetEx: ", opeIdGE)
        Log.v("ZoneActi GetEx: ", tokenGE.toString())

        if (num==1){
            Log.v("Create Pref", "Create Pref")
            createSessionPreference(/*tokenGE.toString(),*/ opeIdGE.toInt())
            getZones()

            Log.v("getRe", "Paso por aqui antesd del getReser")
            //getReservas()
            val listActivity = ListActivity.instance
            listActivity.getReservas()

        }

        val opeIdPref = preferences.getInt("opeId", 0)
        val tokenPref = preferences.getString("tokenPref", null)
        Log.v("ZoneActi ID pref: ", opeIdPref.toString())
        Log.v("ZoneActi token pref: ", tokenPref.toString())

        //getAllZones(tokenGE.toString())
        listeners()

        var listaResevas = emptyList<Reserva>()
        database.reservas().getByCheck1().observe(this, Observer {
            listaResevas = it as ArrayList<Reserva>

            for (i in listaResevas.indices) {
                CoroutineScope(Dispatchers.IO).launch{
                    updateReser(listaResevas[i])
                }
            }
           /* CoroutineScope(Dispatchers.IO).launch{
               database.reservas().deleteByCheckID()
            }*/
        })

        //syncDBServerToDBLocalPersons()
    }



    private fun updateReser(reserva: Reserva) {
        Log.v("Update","Entro: " + reserva)
        val serviceImpl = ServiceImpl()
        serviceImpl.updateReserve(this, reserva) { ->
            run {}
        }
    }


    private fun getZones() {
        val tokenPref = preferences.getString("tokenPref", null)
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getAll(this, tokenPref.toString()) { response ->
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
                /*val url = "http://192.168.1.129:8000/img/"
                val imageUrl = url + response?.url_img + ".jpg"
                Picasso.with(this).load(imageUrl).into(bg_lists);*/
            }
        }
    }
    private fun syncDBServerToDBLocalPersons() {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getAllPerson(this) { response ->
            run {
                val database = AppDatabase.getDatabase(this)
                CoroutineScope(Dispatchers.IO).launch{
                    database.personas().delete()
                    Log.v("DBBorrao", "BD Borrada, personas vacia")
                    val reservaArray : ArrayList<Persona>? = response
                    if (reservaArray != null) {
                        for (i in 0 until reservaArray.size) {
                            Log.v("DBInsert", "Insertadas")
                            database.personas().insert(reservaArray[i])
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
        val helpBtn = findViewById<ImageButton>(R.id.buttonToHelp)
        helpBtn.setOnClickListener {
            val intent = Intent(this, WebView::class.java)
            startActivity(intent)
        }

        val profibleBtn = findViewById<ImageView>(R.id.avatarProfile2)
        profibleBtn.setOnClickListener {
            val intent = Intent(this, UserProfileActivity::class.java)
            startActivity(intent)
        }
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

    private fun createSessionPreference(/*tokenPref: String,*/ opeId: Int) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        //preferences["tokenPref"] = tokenPref
        preferences["opeId"] = opeId
    }


}




