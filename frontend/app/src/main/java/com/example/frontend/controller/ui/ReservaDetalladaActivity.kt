package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.controller.models.*
import com.example.frontend.controller.util.PreferenceHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opencanarias.pruebasync.util.AppDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_reserva_detallada.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class ReservaDetalladaActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    private lateinit var liveData: LiveData<Zone>

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reserva_detallada)
        val timeZone= "GMT+1"
        //val reservaId = this.intent.getIntExtra("reservaId", 1)
        textTextArea.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")
        val reservaId = preferences.getInt("reservaSearchId", 1)
        //getZone(zoneId)

        //getBooking(reservaId)

        var getReservas = emptyList<Reserva>()
        var listaZones = emptyList<Zone>()
        var listaOperarios = emptyList<Operario>()
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

            val JSONSolicitante = getReservas[0].incidencias

            val gson = Gson()
            val mapType = object: TypeToken<Map<String, Any>>() {}.type
            var inci: Map<String, Any> = gson.fromJson(JSONSolicitante, mapType)
            Log.v("aaa", inci.toString())

            val arrayIncidencyaEspecType = object: TypeToken<ArrayList<IncidenciaEspec>>() {}.type

            val plazasMAP = "["+gson.toJson(inci["plazas"]).toString()+"]"
            Log.v("bbb", inci["plazas"].toString())
            Log.v("ccc", "+ "+plazasMAP+" +")

            //val textArea: EditText = findViewById(R.id.textTextArea)

            var inciPlazasMap: ArrayList<IncidenciaEspec> = gson.fromJson(plazasMAP, arrayIncidencyaEspecType)
            val casetasMaP = "["+gson.toJson(inci["casetas"])+"]"
            var inciCasetasMaP: ArrayList<IncidenciaEspec> = gson.fromJson(casetasMaP, arrayIncidencyaEspecType)
            val caravanaMaP = "["+gson.toJson(inci["caravanas"])+"]"
            var inciCaravanaMaP: ArrayList<IncidenciaEspec> = gson.fromJson(caravanaMaP, arrayIncidencyaEspecType)
            val solicitanteMaP = "["+gson.toJson(inci["solicitante"])+"]"
            var inciSolicitanteMaP: ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMaP, arrayIncidencyaEspecType)
            val acompañantesMaP = gson.toJson(inci["acompañantes"])
            var inciAcompañantesMaP: ArrayList<IncidenciaEspec> = gson.fromJson(acompañantesMaP, arrayIncidencyaEspecType)
            val observacionesMaP = gson.toJson(inci["observaciones"])
            var inciObservacionesMaP: ArrayList<IncidenciaEspec> = gson.fromJson(observacionesMaP, arrayIncidencyaEspecType)


            if(inciCasetasMaP[0].texto != ""){
                textTextArea.setText("\n"+inciCasetasMaP[0].texto + " - " + inciCasetasMaP[0].fechahora + " - " + inciCasetasMaP[0].idusuario)
            }
            if(inciCaravanaMaP[0].texto != ""){
                textTextArea.setText("\n"+inciCaravanaMaP[0].texto + " - " + inciCaravanaMaP[0].fechahora + " - " + inciCaravanaMaP[0].idusuario + " " + textTextArea.text)
            }
            if(inciSolicitanteMaP[0].texto != ""){
                textTextArea.setText("\n"+inciSolicitanteMaP[0].texto + " - " + inciSolicitanteMaP[0].fechahora + " - " + inciCasetasMaP[0].idusuario + " " + textTextArea.text)
            }


            if(getReservas[0].checkin == "1"){
                buttonCheckIn.setBackgroundResource(R.drawable.button_checked)
                val ope_id = preferences.getInt("opeId", 1)
                database.operarios().getById(ope_id).observe(this, Observer {
                    listaOperarios = it
                    val textCheckIn =
                        "CHECK IN: Llevado a cabo por " + listaOperarios[0].nombre + " con DNI " + listaOperarios[0].dni + " - " + fechaCheckin
                    textTextArea.setText("\n" + textCheckIn + textTextArea.text)
                })
            }else{
                buttonCheckIn.setBackgroundResource(R.drawable.button_checkin)
            }

            textEntrada.setText(fecha_entrada)
            textEntrada2.setText(fecha_salida)

            database.zonas().getById(zoneId).observe(this, Observer{
                listaZones = it
                localization.text =listaZones[0].localizacion
                name.text = listaZones[0].nombre
            })

            logo.setOnClickListener {
                val reserv = Reserva(reservaId, reservaIdPersona, dni, fecha_entrada, fecha_salida, localizador, num_personas, acompanantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matriculas, checkin, fechaCheckin, incidencia, incidencias, estado, zonaId)
                CoroutineScope(Dispatchers.IO).launch{
                    database.reservas().delete(reserv)
                }
                val intent = Intent(this, ListActivity::class.java)
                startActivity(intent)
            }

            val reserva = Reserva(reservaId, reservaIdPersona, dni, fecha_entrada, fecha_salida, localizador, num_personas, acompanantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matriculas, "1", obtenerFechaActualMoreTime(timeZone).toString(), incidencia, incidencias, estado, zoneId)

            buttonCheckIn.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                            }
                    val intent = Intent(this, ZoneActivity::class.java)
                    startActivity(intent)
                    finish()
            }
        })

        buttonGestion.setOnClickListener {
            goToIncidencias()
        }

        //getPerson(personId)
    }

    private fun goToIncidencias(){
        val intent = Intent(this, IncidenciasListActivity::class.java)
        startActivity(intent)
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
        val formato = "dd/MM/YYYY"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun obtenerFechaActualMoreTime(zonaHoraria: String?): String? {
        val formato = "dd/MM/YYYY hh:mm:ss"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

}