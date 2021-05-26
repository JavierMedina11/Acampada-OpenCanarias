package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.frontend.R
import com.example.frontend.controller.models.*
import com.example.frontend.controller.util.IncidenciaMatriculaAdapter
import com.example.frontend.controller.util.IncidenciasAcompañantesAdapter
import com.example.frontend.controller.util.PreferenceHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_incidencias_list.*
import kotlinx.android.synthetic.main.activity_reserva_detallada.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import com.example.frontend.controller.util.PreferenceHelper.set

class IncidenciasListActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
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
        val database = AppDatabase.getDatabase(this)

        val data = obtenerFechaActual(timeZone).toString()

        database.reservas().getById(7)
            .observe(this, Observer {
                getReservas = it

                val reservaIdPersona = getReservas[0].id_persona
                numPersonas.text = getReservas[0].num_personas.toString() + " personas"

                database.personas().getById(reservaIdPersona).observe(this, Observer {
                    getPersonas = it
                    solicitanteName.text = getPersonas[0].nombre + " " + getPersonas[0].apellido1 + " " + getPersonas[0].apellido2
                    solicitanteDNI.text = getPersonas[0].dni
                    solicitanteFecha.text = getPersonas[0].fecha_nacimiento
                    solicitanteTlf.text = getPersonas[0].telefono
                    val JSONSolicitante = getReservas[0].incidencias

                    val gson = Gson()
                    val mapType = object: TypeToken<Map<String, Any>>() {}.type
                    var inci: Map<String, Any> = gson.fromJson(JSONSolicitante, mapType)

                    val arrayIncidencyaEspecType = object: TypeToken<java.util.ArrayList<IncidenciaEspec>>() {}.type

                    val plazasMAP = "["+gson.toJson(inci["plazas"]).toString()+"]"
                    var inciPlazasMap: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(plazasMAP, arrayIncidencyaEspecType)
                    val casetasMaP = "["+gson.toJson(inci["casetas"])+"]"
                    var inciCasetasMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(casetasMaP, arrayIncidencyaEspecType)
                    val caravanaMaP = "["+gson.toJson(inci["caravanas"])+"]"
                    var inciCaravanaMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(caravanaMaP, arrayIncidencyaEspecType)
                    val solicitanteMaP = "["+gson.toJson(inci["solicitante"])+"]"
                    var inciSolicitanteMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMaP, arrayIncidencyaEspecType)

                    caravanaText.text = getReservas[0].num_caravanas.toString()
                    campañaText.text = getReservas[0].num_casetas.toString()

                    if(inciCasetasMaP[0].texto != ""){
                        imageButton4.setBackgroundResource(R.drawable.bg_button2)
                    }else if(inciCasetasMaP[0].texto == ""){
                        solicitanteBG.setBackgroundResource(R.drawable.bg_button)
                    }
                    if(inciCaravanaMaP[0].texto != ""){
                        imageButton5.setBackgroundResource(R.drawable.bg_button2)
                    }else if(inciCaravanaMaP[0].texto == ""){
                        solicitanteBG.setBackgroundResource(R.drawable.bg_button)
                    }
                    if(inciSolicitanteMaP[0].texto != ""){
                        solicitanteBG.setBackgroundResource(R.drawable.solici_ultimate)
                    }else if(inciSolicitanteMaP[0].texto == ""){
                        solicitanteBG.setBackgroundResource(R.drawable.solici_no_checked)
                    }

                    buttonNO.setOnClickListener {
                        solicitanteBG.setBackgroundResource(R.drawable.solici_ultimate)
                        preferences["stateSolicitante"] = "false"

                    }

                    buttonCheck.setOnClickListener {
                        solicitanteBG.setBackgroundResource(R.drawable.solici_no_checked)
                        preferences["stateSolicitante"] = "true"
                        val stateSolicitante = preferences.getString("stateSolicitante", "1").toString()
                        Log.v("QQQQQQQQQQQQQQQQQQQ", stateSolicitante)

                        val jObject:JSONObject  = JSONObject(JSONSolicitante);
                        jObject.getJSONObject("solicitante").put("texto", "");
                        jObject.getJSONObject("solicitante").put("fechahora", "");
                        jObject.getJSONObject("solicitante").put("idusuario","");

                        val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada,
                            getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas,
                            getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                        CoroutineScope(Dispatchers.IO).launch{
                            delay(5000)
                            database.reservas().update(reserva)
                            Log.v("FUNCIONA", "Insertadas")
                        }
                    }

                    imageButton4.setOnClickListener {
                        if(inciCasetasMaP[0].texto != ""){
                            imageButton4.setBackgroundResource(R.drawable.bg_button)

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
                            Log.v("Casetas", "+ "+casetasMaP+" +")
                        }else if(inciCasetasMaP[0].texto == ""){
                            imageButton4.setBackgroundResource(R.drawable.bg_button2)

                            val jObject:JSONObject  = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("casetas").put("texto", "El número de casetas supera el número indicado en la reserva");
                            jObject.getJSONObject("casetas").put("fechahora", data);
                            jObject.getJSONObject("casetas").put("idusuario",1);

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada,
                                getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas,
                                getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch{
                                delay(5000)
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                            Log.v("Casetas", "+ "+casetasMaP+" +")
                        }
                    }
                    imageButton5.setOnClickListener {
                        if (inciCaravanaMaP[0].texto != "") {
                            imageButton5.setBackgroundResource(R.drawable.bg_button)

                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("caravanas").put("texto", "");
                            jObject.getJSONObject("caravanas").put("fechahora", "");
                            jObject.getJSONObject("caravanas").put("idusuario", "");

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva,
                                getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas,
                                getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(5000)
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                        } else if (inciCaravanaMaP[0].texto == "") {
                            imageButton5.setBackgroundResource(R.drawable.bg_button2)

                            val jObject: JSONObject = JSONObject(JSONSolicitante);
                            jObject.getJSONObject("caravanas").put("texto", "El número de caranas supera el número indicado en la reserva");
                            jObject.getJSONObject("caravanas").put("fechahora", data);
                            jObject.getJSONObject("caravanas").put("idusuario", 1);

                            val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva,
                                getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas,
                                getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(5000)
                                database.reservas().update(reserva)
                                Log.v("FUNCIONA", "Insertadas")
                            }
                        }
                    }
                })
                val acompanantes = getReservas[0].acompanantes
                val gson = Gson()
                val arrayPersonaType = object : TypeToken<ArrayList<Persona>>() {}.type
                var personitas: ArrayList<Persona> = gson.fromJson(acompanantes, arrayPersonaType)

                val matriculas = getReservas[0].matriculas
                val arrayMatriculaType = object : TypeToken<ArrayList<Matricula>>() {}.type
                var matriculas2: ArrayList<Matricula> = gson.fromJson(matriculas, arrayMatriculaType)

                viewAdapter = IncidenciasAcompañantesAdapter(personitas, this)
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
        val matri = Matricula(1, "AAA", "AAA")
        val matri2 = Matricula(1, "AAA", "AAA")
        val matri3 = Matricula(1, "AAA", "AAA")
        matriculasddd.add(matri)
        matriculasddd.add(matri2)
        matriculasddd.add(matri3)

        val database2 = AppDatabase.getDatabase(this)

        database2.zonas().getAll().observe(this, Observer {
            listaMatriculas = it as ArrayList<Matricula>

            val adapter = IncidenciaMatriculaAdapter(matriculasddd as ArrayList<Matricula>, this)
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
                            alpha = (MIN_ALPHA + (((scaleFactor - MIN_SCALE) / (1 - MIN_SCALE)) * (1 - MIN_ALPHA)))
                        }
                        else -> {
                            alpha = 1f
                            rotationX = 0f
                        }
                    }
                }
            }
        })

        buttonMatri.setOnClickListener{
            dialogs.show()
        }

        buttonObservaciones.setOnClickListener{
            toReservaDetallada()
        }

        buttonGuardar.setOnClickListener {
            guardarDatos()
        }
    }

    private fun toReservaDetallada(){
        val intent = Intent(this, ReservaDetalladaActivity::class.java)
        startActivity(intent)
    }

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

                    if(stateSolicitante == "true") {
                        val jObject:JSONObject  = JSONObject(JSONSolicitante);
                        jObject.getJSONObject("solicitante").put("texto", "No se ha podido verificar la identidad de" + getPersonas[0].nombre + " " + getPersonas[0].apellido1 + " " + getPersonas[0].apellido2);
                        jObject.getJSONObject("solicitante").put("fechahora", data);
                        jObject.getJSONObject("solicitante").put("idusuario",1);

                        val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada,
                            getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas,
                            getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                        CoroutineScope(Dispatchers.IO).launch{
                            delay(5000)
                            database.reservas().update(reserva)
                            Log.v("FUNCIONA", "Insertadas")
                        }
                    }else if(stateSolicitante == "false"){
                        val jObject:JSONObject  = JSONObject(JSONSolicitante);
                        jObject.getJSONObject("solicitante").put("texto", "");
                        jObject.getJSONObject("solicitante").put("fechahora", "");
                        jObject.getJSONObject("solicitante").put("idusuario","");

                        val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada,
                            getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas, getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas,
                            getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin, "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                        CoroutineScope(Dispatchers.IO).launch{
                            delay(5000)
                            database.reservas().update(reserva)
                            Log.v("FUNCIONA", "Insertadas")
                        }
                    }
                })
            })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun modalListener(preferences: SharedPreferences, context: Context) {
        Log.v("SSSSSSSSS", "ssssssssssssssssss")
        val acompañante_selected_nombre = preferences.getString("acompañante_selected_nombre", "1").toString()
        val acompañante_selected_apellido1 = preferences.getString("acompañante_selected_apellido1", "1").toString()
        val acompañante_selected_apellido2 = preferences.getString("acompañante_selected_apellido2", "1").toString()

        val database = AppDatabase.getDatabase(context)
        var getReservas = emptyList<Reserva>()
        val data = obtenerFechaActual(timeZone).toString()
        val reservaId = preferences.getInt("reservaSearchId", 1)

        database.reservas().getById(7)
            .observe(this, Observer {
                getReservas = it
                Log.v("bbb", getReservas.size.toString())
                val JSONAcompañantes = getReservas[0].incidencias

                val jObject:JSONObject  = JSONObject(JSONAcompañantes);
                val compObject:JSONObject  = JSONObject();
                compObject.put("texto", "No se ha podido verificar la identidad de" + acompañante_selected_nombre + " " + acompañante_selected_apellido1 + " " + acompañante_selected_apellido2);
                compObject.put("fechahora", data);
                compObject.put("idusuario",1);
                jObject.put("acompañante", compObject)

                Log.v("bbb", jObject.toString())
            })
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

}


