package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Operario
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.util.ZoneAdapter
import com.example.frontend.databinding.ActivityZoneBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.example.frontend.controller.util.PreferenceHelper
import com.example.frontend.controller.util.PreferenceHelper.set
import com.opencanarias.pruebasync.util.AppDatabase
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

        database.zonas().getAll().observe(this,  Observer{
            listaZones = it as ArrayList<Zone>

            val adapter =  ZoneAdapter(listaZones as ArrayList<Zone>, this)
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

                            alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                        }
                        else -> {
                            alpha = 0f
                        }
                    }
                }
            }
        })

        val num : Int = this.intent.getIntExtra("num",0)

        val tokenGE : String? = this.intent.getStringExtra("api_token")
        val opeIdGE : String = this.intent.getStringExtra("opeId").toString()
        Log.v("ZoneActi GetEx: ",opeIdGE)
        Log.v("ZoneActi GetEx: ",tokenGE.toString())

        if (num==1){
            Log.v("Create Pref","Create Pref")
            createSessionPreference(tokenGE.toString(), opeIdGE.toInt())
        }

        val opeIdPref = preferences.getInt("opeId", 0)
        val tokenPref = preferences.getString("tokenPref", null)
        Log.v("ZoneActi ID pref: ", opeIdPref.toString())
        Log.v("ZoneActi token pref: ",tokenPref.toString())

        //getAllZones(tokenGE.toString())
        listeners()
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

    private fun createSessionPreference(tokenPref: String, opeId: Int) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["tokenPref"] = tokenPref
        preferences["opeId"] = opeId
    }


}




