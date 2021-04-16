package com.example.frontend.controller.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frontend.R
import kotlinx.android.synthetic.main.activity_home.*
import org.java_websocket.client.WebSocketClient
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

        /*val typesOfNum = arrayOf("1", "3", "5")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, typesOfNum)

        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerResult.text = typesOfNum[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

                <ImageView
        android:id="@+id/imageView"
        android:layout_width="265dp"
        android:layout_height="80dp"
        android:layout_marginTop="-175dp"
        app:srcCompat="@drawable/input_search" />

    <EditText
        android:id="@+id/editTextPutoAmo"
        android:layout_width="100dp"
        android:layout_height="40dp"
        android:layout_marginTop="25dp"
        android:hint="Localizador"
        android:textAlignment="center"
        android:textColor="@color/earth"
        android:textColorHint="@color/earth"
        android:textSize="16sp"
        android:translationX="57.5dp"
        android:translationY="-90dp"
        app:layout_constraintEnd_toEndOf="@+id/imageView"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="@+id/imageView"
        app:layout_constraintTop_toTopOf="@+id/imageView" />

                <ImageButton
        android:id="@+id/earthSearch"
        android:layout_width="688dp"
        android:layout_height="243dp"
        android:layout_marginLeft="-185dp"
        android:layout_marginTop="250dp"
        android:background="@drawable/earth_search"
        android:textColor="@color/black"
        android:textSize="17sp"
        android:transitionName="buttonHelp" />
        }*/

        buttonToLogin.setOnClickListener {
            goToLoginActivity()
        }

        button3.setOnClickListener {
            addDBLocalBaseData()
        }
    }

    private fun addDBLocalBaseData(){
            val operario = Operario(0, "tete@tete.com", "123456", "Tete","12345678T", "")
            val zone = Zone(0, "Llanos del Salado", "San Mateo", "prueba")
            val zone2 = Zone(0, "Llanos de la Mimbre", "Agaete", "prueba")
            val zone3 = Zone(0, "Llanos de la pez", "Tejeda", "prueba")
            val reserva = Reserva(0, 1, "42269273b",  "2021-03-23", "2021-03-24","AB51", 1, 1, "0", "0", 1)
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
            val persona = Persona(0, "Javi", "Medina", "42269273b", "persona1")
            val persona2 = Persona(0, "Tete", "Tetaso", "12345666Y", "persona2")
            val persona3 = Persona(0, "Maria", "Planta", "22446688R", "persona3")

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
                database.zonas().insert(zone3)*/

                database.operarios().insert(operario)

                /*database.personas().insert(persona)
                database.personas().insert(persona2)
                database.personas().insert(persona3)*/
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
