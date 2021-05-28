package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.R
import com.example.frontend.controller.models.*
import com.example.frontend.controller.util.*
import com.example.frontend.controller.util.PreferenceHelper.set
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_incidencias_list.*
import kotlinx.android.synthetic.main.activity_reserva_detallada.*
import kotlinx.android.synthetic.main.item_container_acompanantes.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class IncidenciasListActivity : AppCompatActivity(), RowClickListener, RowClickListener2 {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemClickListener(acompañanteEntity: Persona) {
        acompañanteEntity.nombre

        val database = AppDatabase.getDatabase(this)
        var getReservas = emptyList<Reserva>()
        var getPersonas = emptyList<Persona>()
        val data = obtenerFechaActual(timeZone).toString()
        val reservaId = preferences.getInt("reservaSearchId", 1)
        val gson = Gson()

        val reservas_id_persona = preferences.getInt("reservas_id_persona", 1)
        val reservas_dni_persona = preferences.getString("reservas_dni_persona", "1")
        val reservas_fecha_entrada = preferences.getString("reservas_fecha_entrada", "1")
        val reserva_localizador = preferences.getString("reserva_localizador", "1")
        val reservas_fecha_salida = preferences.getString("reservas_fecha_salida", "1")
        val reservas_num_personas = preferences.getInt("reservas_num_personas", 1)
        val reservas_acompanantes = preferences.getString("reservas_acompanantes", "1")
        val reservas_num_vehiculos = preferences.getInt("reservas_num_vehiculos", 1)
        val reservas_num_bus = preferences.getInt("reservas_num_bus", 1)
        val reservas_num_caravanas = preferences.getInt("reservas_num_caravanas", 1)
        val reservas_num_casetas = preferences.getInt("reservas_num_casetas", 1)
        val reservas_matriculas = preferences.getString("reservas_matriculas", "1")
        val reservas_estado = preferences.getString("reservas_estado", "1")
        val reservas_checkin = preferences.getString("reservas_checkin", "1")
        val reservas_fecha_checkin = preferences.getString("reservas_fecha_checkin", "1")
        val reservas_incidencia = preferences.getString("reservas_incidencia", "1")
        val reservas_incidencias = preferences.getString("reservas_incidencias", "1")
        val reservas_id_zona = preferences.getInt("reservas_id_zona", 1)

       val JSONAcompañante = reservas_acompanantes
        //Log.v("DDDDDDDDDDDDDDDDDD", JSONAcompañante.toString())
        val arrayPersonaType = object: TypeToken<ArrayList<Persona>>() {}.type
        var personitas: ArrayList<Persona> = gson.fromJson(JSONAcompañante, arrayPersonaType)
        Log.v("DDDDDDDDDDDDDDDDDD", personitas[acompañanteEntity.localizador].estado)

        val p:Persona = Persona(
            0,
            acompañanteEntity.nombre,
            acompañanteEntity.apellido1,
            acompañanteEntity.apellido2,
            "",
            acompañanteEntity.dni,
            "",
            "",
            "",
            "",
            acompañanteEntity.url_img,
            acompañanteEntity.localizador,
            "Checked"
        )

        personitas.set(acompañanteEntity.localizador, p)

        val gson33 = Gson()
        val gson50 = Gson()

        preferences["reservas_acompanantes"] = gson33.toJson(personitas).toString()
        Log.v(
            "PRUEBILLA DE MIERDA 5",
            preferences.getString("reservas_acompanantes", "1").toString()
        )

        val JSONAcompañantes = reservas_incidencias
        val jObject: JSONObject = JSONObject(JSONAcompañantes);

        /*val JSONAcompañantes2 = reservas_incidencias
        val jObject2: JSONObject = JSONObject(JSONAcompañantes);
        val gson2 = Gson()
        val mapType = object: TypeToken<Map<String, Any>>() {}.type
        var inci: Map<String, Any> = gson2.fromJson(JSONAcompañantes2, mapType)
        inci.forEach { println(it) }
        //Log.v("TTTTTTTTTTTT", jObject2.toString())

        val observacionesMAP = gson.toJson(inci["acompañantes"])
        //Log.v("TTTTTTTTTTTT2", observacionesMAP)
        val arrayIncidencyaEspecType = object: TypeToken<java.util.ArrayList<IncidenciaEspec>>() {}.type
        var inciObservacionesMAP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(
            observacionesMAP,
            arrayIncidencyaEspecType
        )
        //Log.v("TTTTTTTTTTTT3", inciObservacionesMAP[acompañanteEntity.localizador].texto)

        val texto = "No se ha podido verificar la identidad de " + acompañanteEntity.nombre + " " + acompañanteEntity.apellido1 + " " + acompañanteEntity.apellido2
        val objIncidenciaEspecifica: IncidenciaEspec = IncidenciaEspec(texto, data, "1")
        inciObservacionesMAP.add(objIncidenciaEspecifica)

        val gson3 = GsonBuilder().create()
        val pasarAJSON = gson3.toJson(inciObservacionesMAP)
        gson3.fromJson(pasarAJSON, JsonObject::class.java)

        //Log.v("TTTTTTTTTTTT5", inciObservacionesMAP[0].texto)

        //val prueba = "{\"fechahora\":\""+inciObservacionesMAP[0].fechahora+"\",\"idusuario\":\""+inciObservacionesMAP[0].idusuario+"\",\"texto\":\""+inciObservacionesMAP[0].texto+"\"},{\"fechahora\":\""+inciObservacionesMAP[1].fechahora+"\",\"idusuario\":\""+inciObservacionesMAP[1].idusuario+"\",\"texto\":\""+inciObservacionesMAP[1].texto+"\"}"
        //Log.v("TTTTTTTTTTTT6", prueba)

        val esperoQueFinal = jObject2.put("acompañantes", pasarAJSON)
        Log.v("TTTTTTTTTTTT4", jObject2.toString())*/

        val compObject: JSONObject = JSONObject();
        val jSONArray = JSONArray()
        compObject.put("texto", "No se ha podido verificar la identidad de " + acompañanteEntity.nombre + " " + acompañanteEntity.apellido1 + " " + acompañanteEntity.apellido2);
        compObject.put("fechahora", data);
        compObject.put("idusuario", 1);
        compObject.put("estado", "Checked");
        jSONArray.put(compObject)
        jObject.put("acompañantes", jSONArray)

        if (reservas_matriculas != null && reservas_dni_persona != null && reservas_fecha_entrada != null && reservas_fecha_salida != null && reserva_localizador != null && reservas_acompanantes != null && reservas_fecha_checkin != null && reservas_estado != null && reservas_id_zona != null && reservas_checkin != null) {
            val reserva: Reserva = Reserva(
                reservaId,
                reservas_id_persona,
                reservas_dni_persona,
                reservas_fecha_entrada,
                reservas_fecha_salida,
                reserva_localizador,
                reservas_num_personas,
                preferences.getString("reservas_acompanantes", "1").toString(),
                reservas_num_vehiculos,
                reservas_num_casetas,
                reservas_num_bus,
                reservas_num_caravanas,
                reservas_matriculas,
                reservas_checkin,
                reservas_fecha_checkin,
                "true",
                jObject.toString(),
                reservas_estado,
                reservas_id_zona
            )
            CoroutineScope(Dispatchers.IO).launch {
                database.reservas().update(reserva)
                Log.v("FUNCIONA", "Insertadas")
            }
        }
        }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemClickListener3(acompañanteEntity: Persona) {
        val acompañanteName: TextView = findViewById(R.id.acompananteName)
        val acompañanteDNI: TextView = findViewById(R.id.acompananteDNI)
        val entrada2: ImageButton = findViewById(R.id.buttonCheck2)
        val kbvLocationRRR: ImageView = findViewById(R.id.kbvLocationRRR)

        acompañanteName.setTextColor(Color.BLACK)
        acompañanteDNI.setTextColor(Color.BLACK)
        entrada2.setBackgroundResource(R.drawable.cheque)
        kbvLocationRRR.setBackgroundResource(R.drawable.inci_no_checked2)

        acompañanteEntity.nombre
        Log.v("asdasd", acompañanteEntity.nombre)

        val database = AppDatabase.getDatabase(this)
        var getReservas = emptyList<Reserva>()
        val data = obtenerFechaActual(timeZone).toString()
        val reservaId = preferences.getInt("reservaSearchId", 1)

        val reservas_id_persona = preferences.getInt("reservas_id_persona", 1)
        val reservas_dni_persona = preferences.getString("reservas_dni_persona", "1")
        val reservas_fecha_entrada = preferences.getString("reservas_fecha_entrada", "1")
        val reserva_localizador = preferences.getString("reserva_localizador", "1")
        val reservas_fecha_salida = preferences.getString("reservas_fecha_salida", "1")
        val reservas_num_personas = preferences.getInt("reservas_num_personas", 1)
        val reservas_acompanantes = preferences.getString("reservas_acompanantes", "1")
        val reservas_num_vehiculos = preferences.getInt("reservas_num_vehiculos", 1)
        val reservas_num_bus = preferences.getInt("reservas_num_bus", 1)
        val reservas_num_caravanas = preferences.getInt("reservas_num_caravanas", 1)
        val reservas_num_casetas = preferences.getInt("reservas_num_casetas", 1)
        val reservas_matriculas = preferences.getString("reservas_matriculas", "1")
        val reservas_estado = preferences.getString("reservas_estado", "1")
        val reservas_checkin = preferences.getString("reservas_checkin", "1")
        val reservas_fecha_checkin = preferences.getString("reservas_fecha_checkin", "1")
        val reservas_incidencia = preferences.getString("reservas_incidencia", "1")
        val reservas_incidencias = preferences.getString("reservas_incidencias", "1")
        val reservas_id_zona = preferences.getInt("reservas_id_zona", 1)

        Log.v("bbb", getReservas.size.toString())
        val JSONAcompañantes = reservas_incidencias

        val jObject: JSONObject = JSONObject(JSONAcompañantes);
        val compObject: JSONObject = JSONObject();
        val jSONArray = JSONArray()
        compObject.put("texto", "");
        compObject.put("fechahora", "");
        compObject.put("idusuario", "");
        compObject.put("estado", "");
        jSONArray.put(compObject)
        jObject.put("acompañantes", jSONArray)

        if (reservas_matriculas != null && reservas_dni_persona != null && reservas_fecha_entrada != null && reservas_fecha_salida != null && reserva_localizador != null && reservas_acompanantes != null && reservas_fecha_checkin != null && reservas_estado != null && reservas_id_zona != null && reservas_checkin != null) {
            val reserva: Reserva = Reserva(
                reservaId,
                reservas_id_persona,
                reservas_dni_persona,
                reservas_fecha_entrada,
                reservas_fecha_salida,
                reserva_localizador,
                reservas_num_personas,
                reservas_acompanantes,
                reservas_num_vehiculos,
                reservas_num_casetas,
                reservas_num_bus,
                reservas_num_caravanas,
                reservas_matriculas,
                reservas_checkin,
                reservas_fecha_checkin,
                "true",
                jObject.toString(),
                reservas_estado,
                reservas_id_zona
            )
            CoroutineScope(Dispatchers.IO).launch {
                database.reservas().update(reserva)
                Log.v("FUNCIONA", "Insertadas")
            }
            //preferences["reservas_incidencias"] = jObject.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemClickListener2(matriculaEntity: Matricula) {
        //val entrada3: ImageView = findViewById(R.id.kbvLocation_op)

        //entrada3.setBackgroundResource(R.drawable.incencia_matricula_i)
        matriculaEntity.matricula
        Log.v("asdasd", matriculaEntity.matricula)

        val database = AppDatabase.getDatabase(this)
        var getReservas = emptyList<Reserva>()
        val data = obtenerFechaActual(timeZone).toString()
        val reservaId = preferences.getInt("reservaSearchId", 1)

        val reservas_id_persona = preferences.getInt("reservas_id_persona", 1)
        val reservas_dni_persona = preferences.getString("reservas_dni_persona", "1")
        val reservas_fecha_entrada = preferences.getString("reservas_fecha_entrada", "1")
        val reserva_localizador = preferences.getString("reserva_localizador", "1")
        val reservas_fecha_salida = preferences.getString("reservas_fecha_salida", "1")
        val reservas_num_personas = preferences.getInt("reservas_num_personas", 1)
        val reservas_acompanantes = preferences.getString("reservas_acompanantes", "1")
        val reservas_num_vehiculos = preferences.getInt("reservas_num_vehiculos", 1)
        val reservas_num_bus = preferences.getInt("reservas_num_bus", 1)
        val reservas_num_caravanas = preferences.getInt("reservas_num_caravanas", 1)
        val reservas_num_casetas = preferences.getInt("reservas_num_casetas", 1)
        val reservas_matriculas = preferences.getString("reservas_matriculas", "1")
        val reservas_estado = preferences.getString("reservas_estado", "1")
        val reservas_checkin = preferences.getString("reservas_checkin", "1")
        val reservas_fecha_checkin = preferences.getString("reservas_fecha_checkin", "1")
        val reservas_incidencia = preferences.getString("reservas_incidencia", "1")
        val reservas_incidencias = preferences.getString("reservas_incidencias", "1")
        val reservas_id_zona = preferences.getInt("reservas_id_zona", 1)

        Log.v("bbb", getReservas.size.toString())
        val JSONAcompañantes = reservas_incidencias

        val jObject: JSONObject = JSONObject(JSONAcompañantes);
        val compObject: JSONObject = JSONObject();
        val jSONArray = JSONArray()
        compObject.put(
            "texto",
            "La matrícula de la caravana " + matriculaEntity.matricula + " no coincide con la indicada en la reserva"
        );
        compObject.put("fechahora", data);
        compObject.put("idusuario", 1);
        jSONArray.put(compObject)
        //Log.v("RRRR", jSONArray.toString())
        jObject.put("matriculas", jSONArray)
        Log.v("SSSS", jObject.toString())

        if (reservas_matriculas != null && reservas_dni_persona != null && reservas_fecha_entrada != null && reservas_fecha_salida != null && reserva_localizador != null && reservas_acompanantes != null && reservas_fecha_checkin != null && reservas_estado != null && reservas_id_zona != null && reservas_checkin != null) {
            val reserva: Reserva = Reserva(
                reservaId,
                reservas_id_persona,
                reservas_dni_persona,
                reservas_fecha_entrada,
                reservas_fecha_salida,
                reserva_localizador,
                reservas_num_personas,
                reservas_acompanantes,
                reservas_num_vehiculos,
                reservas_num_casetas,
                reservas_num_bus,
                reservas_num_caravanas,
                reservas_matriculas,
                reservas_checkin,
                reservas_fecha_checkin,
                "true",
                jObject.toString(),
                reservas_estado,
                reservas_id_zona
            )
            CoroutineScope(Dispatchers.IO).launch {
                database.reservas().update(reserva)
                Log.v("FUNCIONA", "Insertadas")
            }
            //preferences["reservas_incidencias"] = jObject.toString()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onItemClickListener4(matriculaEntity: Matricula) {
        //val entrada3: ImageView = findViewById(R.id.kbvLocation_op)

        //entrada3.setBackgroundResource(R.drawable.incencia_matricula_i)
        matriculaEntity.matricula
        Log.v("asdasd", matriculaEntity.matricula)

        val database = AppDatabase.getDatabase(this)
        var getReservas = emptyList<Reserva>()
        val data = obtenerFechaActual(timeZone).toString()
        val reservaId = preferences.getInt("reservaSearchId", 1)

        val reservas_id_persona = preferences.getInt("reservas_id_persona", 1)
        val reservas_dni_persona = preferences.getString("reservas_dni_persona", "1")
        val reservas_fecha_entrada = preferences.getString("reservas_fecha_entrada", "1")
        val reserva_localizador = preferences.getString("reserva_localizador", "1")
        val reservas_fecha_salida = preferences.getString("reservas_fecha_salida", "1")
        val reservas_num_personas = preferences.getInt("reservas_num_personas", 1)
        val reservas_acompanantes = preferences.getString("reservas_acompanantes", "1")
        val reservas_num_vehiculos = preferences.getInt("reservas_num_vehiculos", 1)
        val reservas_num_bus = preferences.getInt("reservas_num_bus", 1)
        val reservas_num_caravanas = preferences.getInt("reservas_num_caravanas", 1)
        val reservas_num_casetas = preferences.getInt("reservas_num_casetas", 1)
        val reservas_matriculas = preferences.getString("reservas_matriculas", "1")
        val reservas_estado = preferences.getString("reservas_estado", "1")
        val reservas_checkin = preferences.getString("reservas_checkin", "1")
        val reservas_fecha_checkin = preferences.getString("reservas_fecha_checkin", "1")
        val reservas_incidencia = preferences.getString("reservas_incidencia", "1")
        val reservas_incidencias = preferences.getString("reservas_incidencias", "1")
        val reservas_id_zona = preferences.getInt("reservas_id_zona", 1)

        Log.v("bbb", getReservas.size.toString())
        val JSONAcompañantes = reservas_incidencias

        val jObject: JSONObject = JSONObject(JSONAcompañantes);
        val compObject: JSONObject = JSONObject();
        val jSONArray = JSONArray()
        compObject.put("texto", "");
        compObject.put("fechahora", "");
        compObject.put("idusuario", "");
        jSONArray.put(compObject)
        //Log.v("RRRR", jSONArray.toString())
        jObject.put("matriculas", jSONArray)
        Log.v("SSSS", jObject.toString())

        if (reservas_matriculas != null && reservas_dni_persona != null && reservas_fecha_entrada != null && reservas_fecha_salida != null && reserva_localizador != null && reservas_acompanantes != null && reservas_fecha_checkin != null && reservas_estado != null && reservas_id_zona != null && reservas_checkin != null) {
            val reserva: Reserva = Reserva(reservaId, reservas_id_persona, reservas_dni_persona, reservas_fecha_entrada, reservas_fecha_salida, reserva_localizador, reservas_num_personas, reservas_acompanantes, reservas_num_vehiculos, reservas_num_casetas, reservas_num_bus, reservas_num_caravanas, reservas_matriculas, reservas_checkin, reservas_fecha_checkin, "true", jObject.toString(), reservas_estado, reservas_id_zona)
            CoroutineScope(Dispatchers.IO).launch {
                database.reservas().update(reserva)
                Log.v("FUNCIONA", "Insertadas")
            }
            //preferences["reservas_incidencias"] = jObject.toString()
        }
    }

    private lateinit var acompañantes: ArrayList<Persona>
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: IncidenciasAcompañantesAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var matriculA: ArrayList<Matricula>

    private lateinit var zones: ArrayList<Zone>
    private lateinit var viewPager2: ViewPager2
    val MIN_SCALE = 0.85f
    val MIN_ALPHA = 0.5f
    val timeZone = "GMT+1"

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incidencias_list)

        val reservaId = preferences.getInt("reservaSearchId", 1)
        textSolicitante.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")
        textAcompañantes.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")

        acompañantes = ArrayList<Persona>()
        matriculA = ArrayList<Matricula>()

        viewManager = LinearLayoutManager(this)
        recyclerView = findViewById<RecyclerView>(R.id.locationRecyclerPerson)
        recyclerView.layoutManager = viewManager
        recyclerView.itemAnimator

        val zoneId = this.intent.getIntExtra("zoneId", 1)

        var getReservas = emptyList<Reserva>()
        var getPersonas = emptyList<Persona>()
        var listaOperarios = emptyList<Operario>()
        val database = AppDatabase.getDatabase(this)

        val data = obtenerFechaActual(timeZone).toString()

        database.reservas().getById(reservaId)
            .observe(this, Observer {
                getReservas = it

                val reservaIdPersona = getReservas[0].id_persona
                preferences["reservas_id"] = getReservas[0].id
                preferences["reservas_id_persona"] = getReservas[0].id_persona
                preferences["reservas_dni_persona"] = getReservas[0].dni_persona
                preferences["reserva_localizador"] = getReservas[0].localizador_reserva
                preferences["reservas_fecha_entrada"] = getReservas[0].fecha_entrada
                preferences["reservas_fecha_salida"] = getReservas[0].fecha_salida
                preferences["reservas_num_personas"] = getReservas[0].num_personas
                preferences["reservas_acompanantes"] = getReservas[0].acompanantes
                preferences["reservas_num_vehiculos"] = getReservas[0].num_vehiculos
                preferences["reservas_num_bus"] = getReservas[0].num_bus
                preferences["reservas_num_caravanas"] = getReservas[0].num_caravanas
                preferences["reservas_num_casetas"] = getReservas[0].num_casetas
                preferences["reservas_matriculas"] = getReservas[0].matriculas
                preferences["reservas_estado"] = getReservas[0].estado
                preferences["reservas_checkin"] = getReservas[0].checkin
                preferences["reservas_fecha_checkin"] = getReservas[0].fecha_checkin
                preferences["reservas_incidencia"] = getReservas[0].incidencia
                preferences["reservas_incidencias"] = getReservas[0].incidencias
                preferences["reservas_id_zona"] = getReservas[0].id_zona

                numPersonas.text = getReservas[0].num_personas.toString() + " personas"

                database.personas().getById(reservaIdPersona).observe(this, Observer {
                    getPersonas = it
                    solicitanteName.text =
                        getPersonas[0].nombre + " " + getPersonas[0].apellido1 + " " + getPersonas[0].apellido2
                    solicitanteDNI.text = getPersonas[0].dni
                    solicitanteFecha.text = getPersonas[0].fecha_nacimiento
                    solicitanteTlf.text = getPersonas[0].telefono
                    val JSONSolicitante = getReservas[0].incidencias
                    val JSONAcompanantes = getReservas[0].acompanantes

                    val gson = Gson()
                    val mapType = object : TypeToken<Map<String, Any>>() {}.type
                    var inci: Map<String, Any> = gson.fromJson(JSONSolicitante, mapType)
                    val arrayPersonaType = object : TypeToken<ArrayList<Persona>>() {}.type
                    var personitas: ArrayList<Persona> = gson.fromJson(
                        JSONAcompanantes,
                        arrayPersonaType
                    )

                    val arrayIncidencyaEspecType =
                        object : TypeToken<java.util.ArrayList<IncidenciaEspec>>() {}.type

                    val plazasMAP = "[" + gson.toJson(inci["plazas"]).toString() + "]"
                    //var inciPlazasMap: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(plazasMAP, arrayIncidencyaEspecType)
                    val casetasMaP = "[" + gson.toJson(inci["casetas"]) + "]"
                    var inciCasetasMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(casetasMaP, arrayIncidencyaEspecType)
                    val caravanaMaP = "[" + gson.toJson(inci["caravanas"]) + "]"
                    var inciCaravanaMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(caravanaMaP, arrayIncidencyaEspecType)
                    val vehiculosMaP = "[" + gson.toJson(inci["vehiculos"]) + "]"
                    var inciVehiculosMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(vehiculosMaP, arrayIncidencyaEspecType)
                    val busaMaP = "[" + gson.toJson(inci["bus"]) + "]"
                    var inciBusMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(busaMaP, arrayIncidencyaEspecType)
                    val solicitanteMaP = "[" + gson.toJson(inci["solicitante"]) + "]"
                    var inciSolicitanteMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMaP, arrayIncidencyaEspecType)
                    val acompananteMaP = gson.toJson(inci["acompañantes"])
                    var inciAcompananteMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMaP, arrayIncidencyaEspecType)
                    val matriculasMaP = gson.toJson(inci["matriculas"])
                    //Log.v("SDADADASDADDS", matriculasMaP)
                    var inciMatriculasMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMaP, arrayIncidencyaEspecType)

                    caravanaText.text = getReservas[0].num_caravanas.toString()
                    campañaText.text = getReservas[0].num_casetas.toString()
                    vehiculoText.text = getReservas[0].num_vehiculos.toString()
                    busText.text = getReservas[0].num_bus.toString()
                    /*val acompañanteName:TextView = findViewById(R.id.acompananteName)
                    val acompañanteDNI:TextView = findViewById(R.id.acompananteDNI)
                    val entrada2:ImageButton = findViewById(R.id.buttonCheck2)
                    val kbvLocationRRR:ImageView = findViewById(R.id.kbvLocationRRR)*/

                    /*if (personitas[0].estado == "Checked"){
                        acompañanteName.setTextColor(Color.WHITE)
                        acompañanteDNI.setTextColor(Color.WHITE)
                        entrada2.setBackgroundResource(R.drawable.cheque2)
                        kbvLocationRRR.setBackgroundResource(R.drawable.incidencia)
                    }else if(personitas[0].estado != "Checked"){
                        acompañanteName.setTextColor(Color.WHITE)
                        acompañanteDNI.setTextColor(Color.WHITE)
                        entrada2.setBackgroundResource(R.drawable.cheque)
                        kbvLocationRRR.setBackgroundResource(R.drawable.inci_no_checked2)
                    }*/

                    if (inciCasetasMaP[0].texto != "") {
                        imageButton5.setBackgroundResource(R.drawable.bg_button2)
                    } else if (inciCasetasMaP[0].texto == "") {
                        imageButton5.setBackgroundResource(R.drawable.bg_button)
                    }
                    if (inciCaravanaMaP[0].texto != "") {
                        imageButton4.setBackgroundResource(R.drawable.bg_button2)
                    } else if (inciCaravanaMaP[0].texto == "") {
                        imageButton4.setBackgroundResource(R.drawable.bg_button)
                    }
                    if (inciVehiculosMaP[0].texto != "") {
                        imageButton50.setBackgroundResource(R.drawable.bg_button2)
                    } else if (inciVehiculosMaP[0].texto == "") {
                        imageButton50.setBackgroundResource(R.drawable.bg_button)
                    }
                    if (inciBusMaP[0].texto != "") {
                        imageButton60.setBackgroundResource(R.drawable.bg_button2)
                    } else if (inciBusMaP[0].texto == "") {
                        imageButton60.setBackgroundResource(R.drawable.bg_button)
                    }
                    if (inciSolicitanteMaP[0].texto != "") {
                        solicitanteBG.setBackgroundResource(R.drawable.solici_ultimate)
                    } else if (inciSolicitanteMaP[0].texto == "") {
                        solicitanteBG.setBackgroundResource(R.drawable.solici_no_checked)
                    }

                    buttonNO.setOnClickListener {
                        val dialogs: Dialog = Dialog(this)
                        dialogs.setContentView(R.layout.add_observation)
                        dialogs.show()
                        val enter = dialogs.findViewById<ImageButton>(R.id.button_popuprrrR)
                        val text = dialogs.findViewById<EditText>(R.id.editTextObservation)
                        enter.setOnClickListener {
                            solicitanteBG.setBackgroundResource(R.drawable.solici_ultimate)
                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("solicitante").put("texto", text.text.toString());
                            jObject.getJSONObject("solicitante").put("fechahora", data);
                            jObject.getJSONObject("solicitante").put("idusuario", 1);

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                            Log.v("JOBJECT1", jObject.toString())
                            dialogs.dismiss()
                        }
                    }

                    buttonCheck.setOnClickListener {
                        solicitanteBG.setBackgroundResource(R.drawable.solici_no_checked)
                        preferences["stateSolicitante"] = "true"
                        val jObject: JSONObject = JSONObject(JSONSolicitante);
                        jObject.getJSONObject("solicitante").put("texto", "");
                        jObject.getJSONObject("solicitante").put("fechahora", "");
                        jObject.getJSONObject("solicitante").put("idusuario", "");

                        val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                        CoroutineScope(Dispatchers.IO).launch {
                            database.reservas().update(reserva)
                            Log.v("FUNCIONA", "Insertadas")
                        }
                        Log.v("JOBJECT2", jObject.toString())
                    }

                    imageButton5.setOnClickListener {
                        val dialogs: Dialog = Dialog(this)
                        dialogs.setContentView(R.layout.add_observation)
                        if (inciCasetasMaP[0].texto != "") {
                            imageButton5.setBackgroundResource(R.drawable.bg_button)
                            Log.v("VERDE", "VERDE")
                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("casetas").put("texto", "");
                            jObject.getJSONObject("casetas").put("fechahora", "");
                            jObject.getJSONObject("casetas").put("idusuario", "");

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                            Log.v("JOBJECT5", jObject.toString())
                        } else if (inciCasetasMaP[0].texto == "") {
                            dialogs.show()
                            val enter = dialogs.findViewById<ImageButton>(R.id.button_popuprrrR)
                            val text = dialogs.findViewById<EditText>(R.id.editTextObservation)
                            enter.setOnClickListener {
                                imageButton5.setBackgroundResource(R.drawable.bg_button2)
                                Log.v("AMARILLO", "AMARILLO")
                                preferences["stateCasetas"] = "true"
                                val jObject: JSONObject = JSONObject(JSONSolicitante);
                                jObject.getJSONObject("casetas").put("texto", text.text.toString());
                                jObject.getJSONObject("casetas").put("fechahora", data);
                                jObject.getJSONObject("casetas").put("idusuario", 1);

                                val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.reservas().update(reserva)
                                    Log.v("FUNCIONA", "Insertadas")
                                }
                                Log.v("JOBJECT5", jObject.toString())
                                dialogs.dismiss()
                            }
                            }
                    }
                    imageButton4.setOnClickListener {
                        val dialogs: Dialog = Dialog(this)
                        dialogs.setContentView(R.layout.add_observation)
                        if (inciCaravanaMaP[0].texto != "") {
                            imageButton4.setBackgroundResource(R.drawable.bg_button)

                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("caravanas").put("texto", "");
                            jObject.getJSONObject("caravanas").put("fechahora", "");
                            jObject.getJSONObject("caravanas").put("idusuario", "");
                            Log.v("PRUEBA PRUEBITA PRUEBA", jObject.toString())
                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA4", "Insertadas")
                            }
                        } else if (inciCaravanaMaP[0].texto == "") {
                            dialogs.show()
                            val enter = dialogs.findViewById<ImageButton>(R.id.button_popuprrrR)
                            val text = dialogs.findViewById<EditText>(R.id.editTextObservation)
                            enter.setOnClickListener {
                                imageButton4.setBackgroundResource(R.drawable.bg_button2)
                                Log.v("AMARILLO", "AMARILLO")
                                preferences["stateCaravanas"] = "true"
                                val jObject: JSONObject = JSONObject(JSONSolicitante);
                                jObject.getJSONObject("caravanas").put("texto", text.text.toString());
                                jObject.getJSONObject("caravanas").put("fechahora", data);
                                jObject.getJSONObject("caravanas").put("idusuario", 1);

                                val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.reservas().update(reserva)
                                    Log.v("FUNCIONA", "Insertadas")
                                }
                                Log.v("JOBJECT5", jObject.toString())
                                dialogs.dismiss()
                            }
                        }
                    }
                    imageButton50.setOnClickListener {
                        val dialogs: Dialog = Dialog(this)
                        dialogs.setContentView(R.layout.add_observation)
                        if (inciVehiculosMaP[0].texto != "") {
                            imageButton50.setBackgroundResource(R.drawable.bg_button)

                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("vehiculos").put("texto", "");
                            jObject.getJSONObject("vehiculos").put("fechahora", "");
                            jObject.getJSONObject("vehiculos").put("idusuario", "");

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                            Log.v("JOBJECT50", jObject.toString())
                        } else if (inciVehiculosMaP[0].texto == "") {
                            dialogs.show()
                            val enter = dialogs.findViewById<ImageButton>(R.id.button_popuprrrR)
                            val text = dialogs.findViewById<EditText>(R.id.editTextObservation)
                            enter.setOnClickListener {
                            imageButton50.setBackgroundResource(R.drawable.bg_button2)

                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("vehiculos").put("texto", text.text.toString());
                            jObject.getJSONObject("vehiculos").put("fechahora", data);
                            jObject.getJSONObject("vehiculos").put("idusuario", 1);

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                            Log.v("JOBJECT50", jObject.toString())
                                dialogs.dismiss()
                        }
                        }
                    }
                    imageButton60.setOnClickListener {
                        val dialogs: Dialog = Dialog(this)
                        dialogs.setContentView(R.layout.add_observation)
                        if (inciBusMaP[0].texto != "") {
                            imageButton60.setBackgroundResource(R.drawable.bg_button)
                            Log.v("VERDE", "VERDE")
                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("bus").put("texto", "");
                            jObject.getJSONObject("bus").put("fechahora", "");
                            jObject.getJSONObject("bus").put("idusuario", "");

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                            Log.v("JOBJECT60", jObject.toString())
                        } else if (inciBusMaP[0].texto == "") {
                            dialogs.show()
                            val enter = dialogs.findViewById<ImageButton>(R.id.button_popuprrrR)
                            val text = dialogs.findViewById<EditText>(R.id.editTextObservation)
                            enter.setOnClickListener {
                                imageButton60.setBackgroundResource(R.drawable.bg_button2)
                                Log.v("AMARILLO", "AMARILLO")
                                val jObject: JSONObject = JSONObject(JSONSolicitante);
                                jObject.getJSONObject("bus").put("texto", text.text.toString());
                                jObject.getJSONObject("bus").put("fechahora", data);
                                jObject.getJSONObject("bus").put("idusuario", 1);

                                val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.reservas().update(reserva)
                                    Log.v("FUNCIONA", "Insertadas")
                                }
                                Log.v("JOBJECT60", jObject.toString())
                                dialogs.dismiss()
                            }
                        }
                    }

                    val buttonCheckIn2 = findViewById<ImageButton>(R.id.buttonCheckIn2)

                    if (getReservas[0].checkin == "1") {
                        buttonCheckIn2.setBackgroundResource(R.drawable.button_checked)
                    } else {
                        buttonCheckIn2.setBackgroundResource(R.drawable.button_checkin)
                    }

                    val reserva = Reserva(
                        reservaId,
                        reservaIdPersona,
                        getReservas[0].dni_persona,
                        getReservas[0].fecha_entrada,
                        getReservas[0].fecha_salida,
                        getReservas[0].localizador_reserva,
                        getReservas[0].num_personas,
                        getReservas[0].acompanantes,
                        getReservas[0].num_vehiculos,
                        getReservas[0].num_casetas,
                        getReservas[0].num_bus,
                        getReservas[0].num_caravanas,
                        getReservas[0].matriculas,
                        "1",
                        obtenerFechaActualMoreTime(
                            timeZone
                        ).toString(),
                        getReservas[0].incidencia,
                        getReservas[0].incidencias,
                        getReservas[0].estado,
                        getReservas[0].id_zona
                    )

                    buttonCheckIn2.setOnClickListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            database.reservas().update(reserva)
                        }
                        val intent = Intent(this, ZoneActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
                val acompanantes = getReservas[0].acompanantes
                val gson = Gson()
                val arrayPersonaType = object : TypeToken<ArrayList<Persona>>() {}.type
                var personitas: ArrayList<Persona> = gson.fromJson(acompanantes, arrayPersonaType)

                val matriculas = getReservas[0].matriculas
                val arrayMatriculaType = object : TypeToken<ArrayList<Matricula>>() {}.type
                var matriculas2: ArrayList<Matricula> = gson.fromJson(
                    matriculas,
                    arrayMatriculaType
                )

                viewAdapter = IncidenciasAcompañantesAdapter(
                    personitas,
                    this,
                    this@IncidenciasListActivity
                )
                recyclerView.adapter = viewAdapter
            })

        val dialogs: Dialog = Dialog(this)
        dialogs.setContentView(R.layout.another_other_matricula_view)

        var listaZones = emptyList<Zone>()
        var listaMatriculas = emptyList<Matricula>()

        val TTTTT: TextView = dialogs.findViewById(R.id.TTTTT)
        TTTTT.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")

        zones = ArrayList<Zone>()
        val matriculasddd = ArrayList<Matricula>()
        val matri = Matricula(1, "AAA", "AAA", 1, "NoCheck")
        val matri2 = Matricula(1, "AAA", "AAA", 2, "NoCheck")
        val matri3 = Matricula(1, "AAA", "AAA", 3, "NoCheck")
        matriculasddd.add(matri)
        matriculasddd.add(matri2)
        matriculasddd.add(matri3)

        val database2 = AppDatabase.getDatabase(this)

        database2.zonas().getAll().observe(this, Observer {
            listaMatriculas = it as ArrayList<Matricula>

            val adapter = IncidenciaMatriculaAdapter(
                matriculasddd as ArrayList<Matricula>,
                this,
                this@IncidenciasListActivity
            )
            val view_pager: ViewPager2 = dialogs.findViewById(R.id.view_pagerrr2)
            viewPager2 = dialogs.findViewById<ViewPager2>(R.id.view_pagerrr2)
            viewPager2.adapter = adapter
            view_pager.setClipToPadding(false)
            view_pager.setClipChildren(false)
            view_pager.setOffscreenPageLimit(3)
            view_pager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)
            view_pager.setPadding(125, 0, 125, 0)
            view_pager.setPageTransformer { page, position ->
                page.apply {
                    when {
                        position < -1 -> {
                            alpha = 1f
                            translationY = 35f
                            rotationX = 0f
                        }
                        position <= 1 -> {
                            val scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position))
                            scaleX = 1.5f
                            scaleY = scaleFactor
                            rotationX = 5f
                            alpha =
                                (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                        }
                        else -> {
                            alpha = 1f
                            rotationX = 0f
                        }
                    }
                }
            }
        })

        goToMatriculas.setOnClickListener{
            dialogs.show()
        }

        buttonObservaciones.setOnClickListener{
            toReservaDetallada()
        }

        //guardarDatos()

    }

    private fun toReservaDetallada(){
        val intent = Intent(this, ReservaDetalladaActivity::class.java)
        startActivity(intent)
    }
/*
    @RequiresApi(Build.VERSION_CODES.N)
    private fun guardarDatos(){
        var getReservas = emptyList<Reserva>()
        var getPersonas = emptyList<Persona>()
        val database = AppDatabase.getDatabase(this)
        val data = obtenerFechaActual(timeZone).toString()

        database.reservas().getById(7)
            .observe(this, Observer {
                getReservas = it
                val reservaIdPersona = getReservas[0].id_persona
                database.personas().getById(reservaIdPersona).observe(this, Observer {
                    getPersonas = it

                    val JSONSolicitante = getReservas[0].incidencias
                    val stateSolicitante = preferences.getString("stateSolicitante", "1").toString()
                    val stateCasetas = preferences.getString("stateCasetas", "1").toString()
                    buttonGuardar.setOnClickListener {
                        if(stateSolicitante == "true") {

                        }else if(stateSolicitante == "false"){

                        }
                        if(stateCasetas == "true") {

                        }else if(stateCasetas == "false"){
                            val jObject:JSONObject  = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("casetas").put("texto", "");
                            jObject.getJSONObject("casetas").put("fechahora", "");
                            jObject.getJSONObject("casetas").put("idusuario","");

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada,
                                getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas,
                                getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch{
                                delay(5000)
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                        }



                    }

                })
            })
    }*/

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

    @RequiresApi(Build.VERSION_CODES.N)
    fun obtenerFechaActualMoreTime(zonaHoraria: String?): String? {
        val formato = "dd/MM/YYYY hh:mm:ss"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

}


