package com.example.frontend.controller.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.set
import androidx.lifecycle.LiveData
import com.example.frontend.R
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.util.PreferenceHelper
import com.opencanarias.pruebasync.util.AppDatabase
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_incidencias.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class IncidenciasActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private lateinit var liveData: LiveData<Zone>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incidencias)

        val incidenciaPlaza = " \"plazas\": { " +
                " \"idusuario\": \"@idusuario\"," +
                "  \"fechahora\": \"@timestamp_fechahora\"," +
                " \" texto\": \"El número de asistentes es menor del 80% de la reserva\"" +
                " },"

        buttonAddInci.setOnClickListener {

            editTextPersonName.setText("Name")

        }
    }

    private fun createInci(){

        val nombre = editTextPersonName.text
        val incidenciaAcom = " { " +
                " \"acompañante\": { " +
                "\"idusuario\": \"@idusuario\", " +
                " \"fechahora\": \"@timestamp_fechahora\", " +
                " \"texto\": \" No se ha podido verificar la identidad del acompañante "+ nombre + "\" " +
                '}'+
                "},"


        Log.v("Incidencia", incidenciaAcom)
        Toast.makeText(this, "Insertado en incidencias", Toast.LENGTH_SHORT).show()

        var getReservas = emptyList<Reserva>()
        var listaZones = emptyList<Zone>()
        var listaPersons = emptyList<Persona>()

        val reservaId = preferences.getInt("reservaSearchId", 1)

        val database = AppDatabase.getDatabase(this)
        database.reservas().getById(reservaId).observe(this, Observer {
            getReservas = it
            val reservaId = getReservas[0].id
            val incidencia = getReservas[0].incidencia
            val incidencias = getReservas[0].incidencias
            val estado = getReservas[0].estado


            val jsoninci = JSONObject(incidencias).getString("acompañantes")
            Log.v("Incidencia", jsoninci)
            //preguntar como guardar solo la parte de acompañantes

            CoroutineScope(Dispatchers.IO).launch{
                //database.reservas().delete(reserv)
            }
        })


    }

}