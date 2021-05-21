package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Reserva
import androidx.lifecycle.Observer
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.util.PreferenceHelper
import com.opencanarias.pruebasync.util.AppDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reserva_detallada.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ReservaDetalladaActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private lateinit var liveData: LiveData<Zone>

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva_detallada)
        val timeZone= "GMT+1"
        //val reservaId = this.intent.getIntExtra("reservaId", 1)
        val reservaId = preferences.getInt("reservaSearchId", 1)
        //getZone(zoneId)

        //getBooking(reservaId)

        var getReservas = emptyList<Reserva>()
        var listaZones = emptyList<Zone>()
        var listaPersons = emptyList<Persona>()

        val database = AppDatabase.getDatabase(this)
        database.reservas().getById(reservaId).observe(this, Observer{
            getReservas = it
            val reservaId = getReservas[0].id
            val reservaIdPersona = getReservas[0].id_persona
            val dni = getReservas[0].dni_persona
            val fecha_entrada = getReservas[0].fecha_entrada
            val fecha_salida  = getReservas[0].fecha_salida
            val personId = getReservas[0].id_persona
            val zonaId = getReservas[0].id_zona
            val localizador = getReservas[0].localizador_reserva
            val zoneId = getReservas[0].id_zona
            val num_personas = getReservas[0].num_personas
            val num_vehiculos = getReservas[0].num_vehiculos
            val checkin = getReservas[0].checkin
            val fechaCheckin = getReservas[0].fecha_checkin
            val acompanantes = getReservas[0].acompanantes
            val num_casetas = getReservas[0].num_casetas
            val num_bus = getReservas[0].num_bus
            val num_caravanas = getReservas[0].num_caravanas
            val matriculas = getReservas[0].matriculas
            val incidencia = getReservas[0].incidencia
            val incidencias = getReservas[0].incidencias
            val estado = getReservas[0].estado

            if(getReservas[0].checkin == "1"){
                buttonCheckIn.setBackgroundResource(R.drawable.button_checked)
            }else{
                buttonCheckIn.setBackgroundResource(R.drawable.button_checkin)
            }

            textEntrada.setText(fecha_entrada)
            textEntrada2.setText(fecha_salida)
            numPersonsR.setText(num_personas.toString())
            numVehiculosR.setText(num_vehiculos.toString())
            textViewNameDude.setText(personId.toString())

            database.zonas().getById(zoneId).observe(this, Observer{
                listaZones = it
                val url = "https://cryptic-dawn-95434.herokuapp.com/img/"
                localization.text =listaZones[0].localizacion
                name.text = listaZones[0].nombre
                val imageUrl = url + listaZones[0].url_img + ".jpg"
                Picasso.with(this).load(imageUrl).into(bg);
            })

            database.personas().getById(personId).observe(this, Observer{
                listaPersons = it
                val url = "https://cryptic-dawn-95434.herokuapp.com/img/"
                val imageUrl = url + listaPersons[0].url_img + ".png"
                Picasso.with(this).load(imageUrl).into(imagenPerfilReserva);
            })

            logo.setOnClickListener {
                val reserv = Reserva(reservaId, reservaIdPersona, dni, fecha_entrada, fecha_salida, localizador, num_personas, acompanantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matriculas, checkin, fechaCheckin, incidencia, incidencias, estado, zonaId)
                CoroutineScope(Dispatchers.IO).launch{
                    database.reservas().delete(reserv)
                }
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }

            val reserva = Reserva(reservaId, reservaIdPersona, dni, fecha_entrada, fecha_salida, localizador, num_personas, acompanantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matriculas, "1", obtenerFechaActual(timeZone).toString(), incidencia, incidencias, estado, zoneId)

            buttonCheckIn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    database.reservas().update(reserva)
                }
                val intent = Intent(this, ZoneActivity::class.java)
                startActivity(intent)
                finish()
            }
        })

        //getPerson(personId)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    fun obtenerFechaConFormato(formato: String?, zonaHoraria: String?): String? {
        val calendar = Calendar.getInstance()
        val date = calendar.time
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat(formato)
        sdf.setTimeZone(TimeZone.getTimeZone(zonaHoraria))
        return sdf.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun obtenerFechaActual(zonaHoraria: String?): String? {
        val formato = "yyyy-MM-dd"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

/*
    private fun getZone(zoneId: Int) {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getZoneById(this, zoneId) { response ->
            run {
                val url = "https://cryptic-dawn-95434.herokuapp.com/img/"
                val localizacion: TextView = findViewById(R.id.localization)
                val name: TextView = findViewById(R.id.name)
                val roomImg: ImageView = findViewById(R.id.bg)
                val imageUrl = url + response?.url_img + ".jpg"

                localizacion.setText(response?.localizacion ?: "")
                name.setText(response?.nombre ?: "")
                Picasso.with(this).load(imageUrl).into(roomImg);
            }
        }
    }

    private fun getBooking(zoneId: Int) {
        val bicycleServiceImpl = ServiceImpl()
        bicycleServiceImpl.getBookingById(this, zoneId) { response ->
            run {
                val fechaEntrada: TextView = findViewById(R.id.textEntrada)
                val fechaSalida: TextView = findViewById(R.id.textEntrada2)
                val numPerson: TextView = findViewById(R.id.numPersonsR)
                val numVehiculos: TextView = findViewById(R.id.numVehiculosR)

                if(response?.checkin == "1"){
                    buttonCheckIn.setBackgroundResource(R.drawable.bg_button_checked)
                    buttonCheckIn.setText("Checked")
                }else{
                    buttonCheckIn.setBackgroundResource(R.drawable.bg_button)
                }

                fechaEntrada.setText(response?.fecha_entrada ?: "")
                fechaSalida.setText(response?.fecha_salida ?: "")
                numPerson.setText(response?.num_personas.toString() ?: "")
                numVehiculos.setText(response?.num_vehiculos.toString() ?: "")
            }
        }
    }

    private fun getPerson(userId: Int){
        val serviceImpl = ServiceImpl()
        serviceImpl.getPersonById(this, userId) { response ->
            run {
                val url = "http://192.168.56.1:8000/img/"
                val imagePerson: ImageView = findViewById(R.id.imagenPerfilReserva)
                val personNameTV: TextView= findViewById(R.id.textViewNameDude)
                val personNamePUT = this.intent.getStringExtra("personName").toString()
                Log.v("GetPerson", personNamePUT)
                personNameTV.setText(personNamePUT)

                val imageUrl = url + response?.url_img  + ".png"
                Picasso.with(this).load(imageUrl).into(imagePerson);
            }
        }
    }*/


}