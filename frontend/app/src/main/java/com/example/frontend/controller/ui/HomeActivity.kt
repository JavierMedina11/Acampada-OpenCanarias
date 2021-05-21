package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frontend.R
import kotlinx.android.synthetic.main.activity_home.*
import org.java_websocket.client.WebSocketClient
import android.app.ActivityOptions
import android.graphics.Typeface
import android.util.Log
import android.util.Pair
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.*
import com.example.frontend.controller.util.PreferenceHelper
import com.example.frontend.controller.util.PreferenceHelper.set
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_home.imageView
import kotlinx.android.synthetic.main.activity_home.imageView13
import kotlinx.android.synthetic.main.activity_home.imageView2
import kotlinx.android.synthetic.main.activity_zone.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var webSocketClient: WebSocketClient

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private var pref_manual_sync_ = 0

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        textViewSincronizado.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")

        buttonToLogin.setOnClickListener {
            goToLoginActivity()
        }

        buttonToSync.setOnClickListener(this)

        button3.setOnClickListener {
            addDBLocalBaseData()
        }

        getBooking(7)
    }

    private fun getBooking(zoneId: Int) {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getBookingJSONAcompañantes(this, zoneId) { response ->
            run {
                if (response != null) {
                    Log.v("PRUEBITA_PRUEBA", "¡ "+response[0].dni+" !")
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    override fun onClick(v: View?) {
        if (pref_manual_sync_ == 0){
            val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.rotation)
            val animation2: Animation = AnimationUtils.loadAnimation(this, R.anim.wide_add)
            val animation3: Animation = AnimationUtils.loadAnimation(this, R.anim.text_alpha)
            animation.setFillAfter(true)
            animation2.setFillAfter(true)
            animation3.setFillAfter(true)
            buttonToSync.startAnimation(animation)
            buttonToSync2.alpha = 1f
            textViewSincronizado.alpha = 1f
            buttonToSync2.startAnimation(animation2)
            textViewSincronizado.startAnimation(animation3)
            Toast.makeText(this, "Sincronizacion en curso", Toast.LENGTH_SHORT).show()
            Toast.makeText(this, "Ya esta Sincronizado", Toast.LENGTH_SHORT).show()
            pref_manual_sync_++
            syncDBServerToDBLocalPersons()
            syncDBServerToDBLocalZones()
            syncDBLocalToDBServerBookingsChecked()
            syncDBServerToDBLocalBookingsNoChecked()
        }else if (pref_manual_sync_ == 1) {
            Toast.makeText(getApplicationContext(), "Ya esta Sincronizado", Toast.LENGTH_SHORT).show()
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
        Log.v("PRUEBA1", "Todo Bien!")
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
        Log.v("PRUEBA2", "Todo Bien!")
    }

    private fun syncDBLocalToDBServerBookingsChecked(){
        var listaReserva = emptyList<Reserva>()
        val reserva = ArrayList<Reserva>()
        val database = AppDatabase.getDatabase(this)
        val serviceImpl = ServiceImpl()
        database.reservas().getByCheck1().observe(this, Observer {
            listaReserva = it as ArrayList<Reserva>

            for (i in 0 until listaReserva.size) {
                Log.v("Update", "Entro: " + reserva)
                serviceImpl.updateReserve(this, listaReserva[i]) { ->
                    run {
                        Log.v("UPDATED", "Updated: " + listaReserva[i].id)
                    }
                }
            }
        });
        Log.v("PRUEBA3", "Todo Bien!")
    }

    private fun syncDBServerToDBLocalBookingsNoChecked() {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getAllBookingsNoChecked(this) { response ->
            run {
                val database = AppDatabase.getDatabase(this)
                CoroutineScope(Dispatchers.IO).launch{
                    database.reservas().deleteNoChecked("0")
                    Log.v("DBBorrao", "BD Borrada, reservas vacias personas")
                    val reservaArray : ArrayList<Reserva>? = response
                    delay(5000)
                    if (reservaArray != null) {
                        for (i in 0 until reservaArray.size) {
                            Log.v("FUNCIONA", "Insertadas")
                            database.reservas().insert(reservaArray[i])
                        }
                    }
                }
            }
        }
        Log.v("PRUEBA4", "Todo Bien!")
    }

    private fun addDBLocalBaseData(){
        val operario = Operario(0, "tete@tete.com", "123456", "Tete","12345678T", "")

        val database = AppDatabase.getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            database.operarios().insert(operario)
        }
    }

    private fun goToLoginActivity() {
        val options = ActivityOptions.makeSceneTransitionAnimation(
            this,
            Pair.create(textID, "tecx"),
            Pair.create(imageView, "earth"),
            Pair.create(imageView8, "tree1"),
            Pair.create(imageView11, "tree2"),
            Pair.create(imageView9, "tree3"),
            Pair.create(imageView3, "camp"),
            Pair.create(imageView2, "arb"),
            Pair.create(imageView6, "treesback"),
            Pair.create(imageView4, "mountain1"),
            Pair.create(imageView5, "mountain2"),
            Pair.create(imageView13, "clouds"),
            Pair.create(buttonToLogin, "buttonExplore"),
            Pair.create(buttonToRegister, "buttonRegister"),
            Pair.create(imageView7, "tit"),
            Pair.create(imageView10, "email"),
            Pair.create(imageView15, "password"),
            Pair.create(buttonToZone, "buttonZone")
        )

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent, options.toBundle())
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
