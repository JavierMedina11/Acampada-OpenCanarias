package com.example.frontend.controller.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.frontend.R
import androidx.lifecycle.Observer
import com.example.frontend.controller.models.*
import com.example.frontend.controller.util.PreferenceHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_reserva_detallada.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
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
        textTextArea.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")
        val reservaId = preferences.getInt("reservaSearchId", 1)

        var getReservas = emptyList<Reserva>()
        var listaZones = emptyList<Zone>()
        var listaOperarios = emptyList<Operario>()

        val database = AppDatabase.getDatabase(this)
        database.reservas().getById(reservaId).observe(this, Observer{
            getReservas = it


            val JSONSolicitante = getReservas[0].incidencias

            val gson = Gson()
            val mapType = object: TypeToken<Map<String, Any>>() {}.type
            var inci: Map<String, Any> = gson.fromJson(JSONSolicitante, mapType)

            val arrayIncidencyaEspecType = object: TypeToken<ArrayList<IncidenciaEspec>>() {}.type

            val plazasMAP = "["+gson.toJson(inci["plazas"]).toString()+"]"
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
            val matriculasMaP = gson.toJson(inci["matriculas"])
            Log.v("SDADADASDADDS", matriculasMaP)
            var inciMatriculasMaP: java.util.ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMaP, arrayIncidencyaEspecType)


            if(inciCasetasMaP[0].texto != ""){
                textTextArea.setText("\n"+inciCasetasMaP[0].texto + " - " + inciCasetasMaP[0].fechahora + " - " + inciCasetasMaP[0].idusuario)
            }
            if(inciCaravanaMaP[0].texto != ""){
                textTextArea.setText("\n"+inciCaravanaMaP[0].texto + " - " + inciCaravanaMaP[0].fechahora + " - " + inciCaravanaMaP[0].idusuario)
            }
            if(inciSolicitanteMaP[0].texto != ""){
                textTextArea.setText("\n"+inciSolicitanteMaP[0].texto + " - " + inciSolicitanteMaP[0].fechahora + " - " + inciCasetasMaP[0].idusuario)
            }
            if(inciObservacionesMaP[0].texto != ""){
            textTextArea.setText("\n"+inciObservacionesMaP[0].texto + " - " + inciObservacionesMaP[0].fechahora + " - " + inciObservacionesMaP[0].idusuario)
            }
            if(inciAcompañantesMaP[0].texto != ""){
                textTextArea.setText("\n"+inciAcompañantesMaP[0].texto + " - " + inciAcompañantesMaP[0].fechahora + " - " + inciAcompañantesMaP[0].idusuario)
            }
            if(inciMatriculasMaP[0].texto != ""){
                textTextArea.setText("\n"+inciMatriculasMaP[0].texto + " - " + inciMatriculasMaP[0].fechahora + " - " + inciMatriculasMaP[0].idusuario)
            }

            if(getReservas[0].checkin == "1"){
                val ope_id = preferences.getInt("opeId", 1)
                database.operarios().getById(ope_id).observe(this, Observer {
                    listaOperarios = it
                    val textCheckIn =
                        "CHECK IN: Llevado a cabo por " + listaOperarios[0].nombre + " con DNI " + listaOperarios[0].dni + " - " + getReservas[0].fecha_checkin
                    textTextArea.setText("\n" + textCheckIn + textTextArea.text)
                })
            }

            textEntrada.setText(getReservas[0].fecha_entrada)
            textEntrada2.setText(getReservas[0].fecha_salida)

            database.zonas().getById(getReservas[0].id_zona).observe(this, Observer{
                listaZones = it
                localization.text =listaZones[0].localizacion
                name.text = listaZones[0].nombre
            })
        })

        val dialogs: Dialog = Dialog(this)
        dialogs.setContentView(R.layout.add_observation)
        buttonGestion.setOnClickListener {
            goToDialog(dialogs, database, reservaId)
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun goToDialog(dialogs: Dialog, database: AppDatabase, reservaId: Int) {
        dialogs.show()
        val cerrar = dialogs.findViewById<ImageButton>(R.id.buttonClosePopUpSSS)
        val enter = dialogs.findViewById<ImageButton>(R.id.button_popuprrrR)
        val new_observation = dialogs.findViewById<EditText>(R.id.editTextObservation)

        val timeZone = "GMT+1"
        val data = obtenerFechaActualMoreTime(timeZone)
        var getReservas = emptyList<Reserva>()
        var getReservas2 = emptyList<Reserva>()

        //textTextArea.setText(new_observation.text.toString()+" \n"+ textTextArea.text+"\n")


            database.reservas().getById(reservaId).observe(this, Observer {
                getReservas = it
                //Log.v("bbb", getReservas.size.toString())

                val JSONAcompañantes = getReservas[0].incidencias
                val gson = Gson()
                val mapType = object: TypeToken<Map<String, Any>>() {}.type
                var inci: Map<String, Any> = gson.fromJson(JSONAcompañantes, mapType)

                val observacionesMAP = gson.toJson(inci["observaciones"])
                val arrayIncidencyaEspecType = object: TypeToken<ArrayList<IncidenciaEspec>>() {}.type
                var inciObservacionesMAP: ArrayList<IncidenciaEspec> = gson.fromJson(observacionesMAP, arrayIncidencyaEspecType)

                enter.setOnClickListener {
                    val jObject: JSONObject = JSONObject(JSONAcompañantes);
                    val compObject2: JSONObject = JSONObject();
                    Log.v("AAA", inciObservacionesMAP[0].texto)
                    val jSONArray = JSONArray()

                    compObject2.put("texto", new_observation.text.toString());
                    compObject2.put("fechahora", data);
                    compObject2.put("idusuario", 1);
                    jSONArray.put(compObject2)

                    jObject.put("observaciones", jSONArray)
                    Log.v("CCC", jObject.toString())

                    val reserva: Reserva = Reserva(getReservas[0].id, getReservas[0].id_persona, getReservas[0].dni_persona, getReservas[0].fecha_entrada, getReservas[0].fecha_salida, getReservas[0].localizador_reserva, getReservas[0].num_personas,
                        getReservas[0].acompanantes, getReservas[0].num_vehiculos, getReservas[0].num_casetas, getReservas[0].num_bus, getReservas[0].num_caravanas, getReservas[0].matriculas, getReservas[0].checkin, getReservas[0].fecha_checkin,
                        "true", jObject.toString(), getReservas[0].estado, getReservas[0].id_zona)
                    CoroutineScope(Dispatchers.IO).launch {
                        database.reservas().update(reserva)
                        Log.v("FUNCIONA", "Insertadas")
                    }

                    database.reservas().getById(reservaId).observe(this, Observer {
                        getReservas2 = it

                        val JSONAcompañantes2 = getReservas[0].incidencias
                        val gson2 = Gson()
                        val mapType2 = object: TypeToken<Map<String, Any>>() {}.type
                        var inci2: Map<String, Any> = gson2.fromJson(JSONAcompañantes2, mapType2)

                        val observacionesMAP2 = gson.toJson(inci2["observaciones"])
                        val arrayIncidencyaEspecType2 = object: TypeToken<ArrayList<IncidenciaEspec>>() {}.type
                        var inciObservacionesMAP2: ArrayList<IncidenciaEspec> = gson.fromJson(observacionesMAP2, arrayIncidencyaEspecType2)

                        textTextArea.setText("\n"+inciObservacionesMAP2[0].texto + " - " + inciObservacionesMAP2[0].fechahora + " - " + inciObservacionesMAP2[0].idusuario + " " + textTextArea.text)

                    })

                }
            })


        cerrar.setOnClickListener {
            dialogs.dismiss()
        }

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
        val formato = "dd-MM-YYYY"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun obtenerFechaActualMoreTime(zonaHoraria: String?): String? {
        val formato = "dd-MM-YYYY hh:mm:ss"
        return obtenerFechaConFormato(formato, zonaHoraria)
    }

}