package com.example.frontend.controller.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.frontend.R
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.android.synthetic.main.activity_home.*
import org.java_websocket.WebSocket
import org.java_websocket.client.WebSocketClient
import org.java_websocket.framing.Framedata
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import javax.net.SocketFactory
import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.util.Pair
import com.example.frontend.controller.models.*
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var webSocketClient: WebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        buttonToLogin.setOnClickListener {
            goToLoginActivity()
        }


        button3.setOnClickListener {
/* val operario = Operario(0, "tete@tete.com", "123456", "Tete","12345678T", "")
/* val zone Zone(0, "Llanos del Salado", "San Mateo", "prueba")
 val zone2 = Zone(0, "Llanos de la Mimbre", "Agaete", "prueba")
 val zone3 = Zone(0, "Llanos de la pez", "Tejeda", "prueba")*/
 /*val reserva = Reserva(0, 1, "42269273b",  "2021-03-23", "2021-03-24","AB51", 1, 1, "0", "0", 1)
 val reserva2 = Reserva(0, 2, "12345666Y", "2021-03-23", "2021-03-29","AB52", 1, 1, "0", "0", 1)
 val reserva3 = Reserva(0, 3,"22446688R", "2021-03-23", "2021-03-28","AB53", 1, 1, "0", "0", 1)
 val reserva4 = Reserva(0, 1,"42269273b",  "2021-03-23", "2021-03-25","AB54", 1, 1, "0", "0", 1)
 val reserva5 = Reserva(0, 1,"42269273b", "2021-03-23", "2021-03-26","AB55", 1, 1, "0", "0", 1)
 val reserva6 = Reserva(0, 2,"12345666Y", "2021-03-23", "2021-03-27","AB56", 1, 1, "0", "0", 1)
 val reserva7 = Reserva(0, 3,"22446688R", "2021-03-23", "2021-03-25","AB59", 1, 1, "0", "0", 1)
 val reserva8 = Reserva(0, 1,"42269273b", "2021-03-23", "2021-03-24","AB60", 1, 1, "0", "0", 1)
 val reserva9 = Reserva(0, 2, "12345666Y", "2021-03-23", "2021-03-25","AB61", 1, 1, "0", "0", 1)
 val reserva10 = Reserva(0, 1, "42269273b","2021-03-24", "2021-03-25","AB57", 1, 1, "0", "0", 1)
 val reserva11 = Reserva(0, 2,"12345666Y", "2021-03-24", "2021-03-27","AB58", 1, 1, "0", "0", 1)
 val reserva12 = Reserva(0, 3,"22446688R", "2021-03-24", "2021-03-29","AB58", 1, 1, "0", "0", 1)
*/
 val persona = Persona(0, "Javi", "Medina", "42269273b", "persona1")
 val persona2 = Persona(0, "Tete", "Tetaso", "12345666Y", "persona2")
 val persona3 = Persona(0, "Maria", "Planta", "22446688R", "persona3")*/

            val database = AppDatabase.getDatabase(this)
            CoroutineScope(Dispatchers.IO).launch {
                /*database.reservas().insert(reserva)
                database.reservas().insert(reserva2)
                database.reservas().insert(reserva3)
                database.reservas().insert(reserva4)
                database.reservas().insert(reserva5)
                database.reservas().insert(reserva6)
                database.reservas().insert(reserva7)
                database.reservas().insert(reserva8)
                database.reservas().insert(reserva9)
                database.reservas().insert(reserva10)
                database.reservas().insert(reserva11)
                database.reservas().insert(reserva12)

                database.zonas().insert(zone)
                database.zonas().insert(zone2)
                database.zonas().insert(zone3)

                database.operarios().insert(operario)

                database.personas().insert(persona)
                database.personas().insert(persona2)
                database.personas().insert(persona3)*/
            }
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
