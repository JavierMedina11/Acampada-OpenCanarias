package com.example.frontend.controller.io

import android.content.Context
import android.content.Intent
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.frontend.controller.models.*

import com.example.frontend.controller.ui.ZoneActivity
import org.json.JSONObject
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.reflect.TypeToken

import org.json.JSONArray

class ServiceImpl: IVolleyService {

    override fun getAll(context: Context, token: String, completionHandler: (response: ArrayList<Zone>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "zona"
        Log.v("Path: ", path)
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
                { response ->
                    val zoneArray : JSONArray = response
                    var zones: ArrayList<Zone> = ArrayList()
                    for (i in 0 until zoneArray.length()) {
                       val zone = zoneArray.getJSONObject(i)
                        val id = zone.getInt("id")
                        val nombre = zone.getString("nombre")
                        val localizacion = zone.getString("localizacion")
                        val url_img = zone.getString("url_img")

                        zones.add(Zone(id, nombre, localizacion, url_img))
                    }
                    completionHandler(zones)
                },
                { error ->
                    Log.v("Error", "Error on")
                    completionHandler(ArrayList<Zone>())
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

    override fun getAllPerson(context: Context, completionHandler: (response: ArrayList<Persona>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "persona"
        Log.v("Path: ", path)
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
            { response ->
                val zoneArray : JSONArray = response
                var zones: ArrayList<Persona> = ArrayList()
                for (i in 0 until zoneArray.length()) {
                    val zone = zoneArray.getJSONObject(i)
                    val id = zone.getInt("id")
                    val name = zone.getString("nombre")
                    val apellido1 = zone.getString("apellido1")
                    val apellido2 = zone.getString("apellido2")
                    val tipo_documento = zone.getString("tipo_documento")
                    val dni = zone.getString("dni")
                    val fecha_nacimiento = zone.getString("fecha_nacimiento")
                    val mail = zone.getString("mail")
                    val direccion = zone.getString("direccion")
                    val telefono = zone.getString("telefono")
                    val url_img = zone.getString("url_img")

                    zones.add(Persona(id, name, apellido1, apellido2, tipo_documento, fecha_nacimiento, mail, direccion, telefono, dni, url_img))
                }
                completionHandler(zones)
            },
            { error ->
                Log.v("Error", "Error on")
                completionHandler(ArrayList<Persona>())
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

    override fun getZoneById(context: Context, zoneId: Int, completionHandler: (response: Zone?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "zona/" + zoneId
        val objectRequest = JsonObjectRequest(Request.Method.GET, path, null,
                { response ->
                    if(response == null) completionHandler(null)
                    val id = response.getInt("id")
                    val nombre = response.getString("nombre")
                    val localizacion = response.getString("localizacion")
                    val url_img = response.getString("url_img")

                    val zone = Zone(id, nombre, localizacion, url_img)
                    completionHandler(zone)
                },
                { error ->
                    completionHandler(null)
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getPersonById(context: Context, personaId: Int, completionHandler: (response: Persona?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "persona/" + personaId
        val objectRequest = JsonObjectRequest(Request.Method.GET, path, null,
                { response ->
                    if(response == null) completionHandler(null)
                    val id = response.getInt("id")
                    val name = response.getString("nombre")
                    val apellido1 = response.getString("apellido1")
                    val apellido2 = response.getString("apellido2")
                    val tipo_documento = response.getString("tipo_documento")
                    val dni = response.getString("dni")
                    val fecha_nacimiento = response.getString("fecha_nacimiento")
                    val mail = response.getString("mail")
                    val direccion = response.getString("direccion")
                    val telefono = response.getString("telefono")
                    val url_img = response.getString("url_img")

                    val persona = Persona(id, name, apellido1, apellido2, tipo_documento, fecha_nacimiento, mail, direccion, telefono, dni, url_img)
                    completionHandler(persona)
                },
                { error ->
                    completionHandler(null)
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getBookingById(context: Context, zoneId: Int, completionHandler: (response: Reserva?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva/" + zoneId
        val objectRequest = JsonObjectRequest(Request.Method.GET, path, null,
                { response ->
                    if(response == null) completionHandler(null)
                    val id = response.getInt("id")
                    val id_persona = response.getInt("id_persona")
                    val dni_persona = response.getString("dni_persona")
                    val fecha_entrada = response.getString("fecha_entrada")
                    val fecha_salida = response.getString("fecha_salida")
                    val localizador_reserva = response.getString("localizador_reserva")
                    val num_personas = response.getInt("num_personas")
                    val acompañantes = response.getString("acompanantes")
                    val num_vehiculos = response.getInt("num_vehiculos")
                    val num_casetas = response.getInt("num_casetas")
                    val num_bus = response.getInt("num_bus")
                    val num_caravanas = response.getInt("num_caravanas")
                    val matricula = response.getString("matriculas")
                    val checkin = response.getString("checkin")
                    val fecha_checkin = response.getString("fecha_checkin")
                    val incidencia = response.getString("incidencia")
                    val incidencias = response.getString("incidencias")
                    val estado = response.getString("estado")
                    val id_zona = response.getInt("id_zona")

                    val reserva = Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona)
                    completionHandler(reserva)
                },
                { error ->
                    completionHandler(null)
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }


    override fun getBookingJSONAcompañantes(context: Context, zoneId: Int, completionHandler: (response: ArrayList<Persona>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva/" + zoneId
        val objectRequest = JsonObjectRequest(Request.Method.GET, path, null,
            { response ->
                if(response == null) completionHandler(null)
                val acompañantes = response.getString("acompanantes")
                val gson = Gson()
                //val JSONAcompañantes: Persona = gson.fromJson(acompañantes, Persona::class.java)

                val JSONlist = response.getString("acompanantes")
                val arrayPersonaType = object: TypeToken<ArrayList<Persona>>() {}.type
                var personitas: ArrayList<Persona> = gson.fromJson(JSONlist, arrayPersonaType)
                Log.v("PRUEBAZA", "+ "+personitas[0].dni+" +")
                Log.v("PRUEBAZA2", "+ "+personitas[0].nombre+" +")

                completionHandler(personitas)
            },
            { error ->
                completionHandler(null)
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }


    override fun getBookingJSONMatriculas(context: Context, zoneId: Int, completionHandler: (response: ArrayList<Matricula>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva/" + zoneId
        val objectRequest = JsonObjectRequest(Request.Method.GET, path, null,
            { response ->
                if(response == null) completionHandler(null)
                val acompañantes = response.getString("acompanantes")
                val gson = Gson()

                val JSONlist2 = response.getString("matriculas")
                val arrayMatriculaType = object: TypeToken<ArrayList<Matricula>>() {}.type
                var matriculas: ArrayList<Matricula> = gson.fromJson(JSONlist2, arrayMatriculaType)
                Log.v("PRUEBAZA3", "+ "+matriculas[0].matricula+" +")
                Log.v("PRUEBAZA4", "+ "+matriculas[0].tipo+" +")

                completionHandler(matriculas)
            },
            { error ->
                completionHandler(null)
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getBookingJSONIncidencias(context: Context, zoneId: Int, completionHandler: (response: Reserva?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva/" + zoneId
        val objectRequest = JsonObjectRequest(Request.Method.GET, path, null,
            { response ->
                if(response == null) completionHandler(null)
                val gson = Gson()

                val JSONlist3 = response.getString("incidencias")
                val mapType = object: TypeToken<Map<String, Any>>() {}.type
                var inci: Map<String, Any> = gson.fromJson(JSONlist3, mapType)

                val plazasMAP = "["+gson.toJson(inci["plazas"])+"]"
                Log.v("PRUEBAZAESPECIAL", plazasMAP)
                val casetasMAP = "["+gson.toJson(inci["casetas"])+"]"
                val caravanaMAP = "["+gson.toJson(inci["caravanas"])+"]"
                val solicitanteMAP = "["+gson.toJson(inci["solicitante"])+"]"
                val acompañantesMAP = gson.toJson(inci["acompañantes"])
                val observacionesMAP = gson.toJson(inci["observaciones"])

                val arrayIncidencyaEspecType = object: TypeToken<ArrayList<IncidenciaEspec>>() {}.type

                var inciPlazasMap: ArrayList<IncidenciaEspec> = gson.fromJson(plazasMAP, arrayIncidencyaEspecType)
                Log.v("PRUEBAZA6", "+ "+inciPlazasMap[0].texto+" +")
                var inciCasetasMAP: ArrayList<IncidenciaEspec> = gson.fromJson(casetasMAP, arrayIncidencyaEspecType)
                Log.v("PRUEBAZA7", "+ "+inciCasetasMAP[0].texto+" +")
                var inciCaravanaMAP: ArrayList<IncidenciaEspec> = gson.fromJson(caravanaMAP, arrayIncidencyaEspecType)
                Log.v("PRUEBAZA8", "+ "+inciCaravanaMAP[0].texto+" +")
                var inciSolicitanteMAP: ArrayList<IncidenciaEspec> = gson.fromJson(solicitanteMAP, arrayIncidencyaEspecType)
                Log.v("PRUEBAZA9", "+ "+inciSolicitanteMAP[0].texto+" +")
                var inciAcompañantesMAP: ArrayList<IncidenciaEspec> = gson.fromJson(acompañantesMAP, arrayIncidencyaEspecType)
                Log.v("PRUEBAZA10", "+ "+inciAcompañantesMAP[0].texto+" +")
                var inciObservacionesMAP: ArrayList<IncidenciaEspec> = gson.fromJson(observacionesMAP, arrayIncidencyaEspecType)
                Log.v("PRUEBAZA11", "+ "+inciObservacionesMAP[0].texto+" +")

                val reserva = Reserva(0, 0, "42269273b", "ewew", "", "", 0, "", 0, 0, 0, 0, "", "", "", "","", "", 0)
                completionHandler(reserva)
            },
            { error ->
                completionHandler(null)
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getAllBookings(context: Context, completionHandler: (response: ArrayList<Reserva>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva"
        val objectRequest = JsonArrayRequest(Request.Method.GET, path, null,
            { response ->
                val reservaArray: JSONArray = response
                var reservas: ArrayList<Reserva> = ArrayList()
                for (i in 0 until reservaArray.length()) {
                    val reservaAr = reservaArray.getJSONObject(i)
                    val id = reservaAr.getInt("id")
                    val id_persona = reservaAr.getInt("id_persona")
                    val dni_persona = reservaAr.getInt("id_persona")
                    val fecha_entrada = reservaAr.getString("fecha_entrada")
                    val fecha_salida = reservaAr.getString("fecha_salida")
                    val localizador_reserva = reservaAr.getString("localizador_reserva")
                    val num_personas = reservaAr.getInt("num_personas")
                    val acompañantes = reservaAr.getString("acompanantes")
                    val num_vehiculos = reservaAr.getInt("num_vehiculos")
                    val num_casetas = reservaAr.getInt("num_casetas")
                    val num_bus = reservaAr.getInt("num_bus")
                    val num_caravanas = reservaAr.getInt("num_caravanas")
                    val matricula = reservaAr.getString("matriculas")
                    val checkin = reservaAr.getString("checkin")
                    val fecha_checkin = reservaAr.getString("fecha_checkin")
                    val incidencia = reservaAr.getString("incidencia")
                    val incidencias = reservaAr.getString("incidencias")
                    val estado = reservaAr.getString("estado")
                    val id_zona = reservaAr.getInt("id_zona")

                    reservas.add(Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona))
                }
                completionHandler(reservas)
            },
            { error ->
                completionHandler(null)
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getAllBookingsNoChecked(context: Context, completionHandler: (response: ArrayList<Reserva>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva-no-check"
        val objectRequest = JsonArrayRequest(Request.Method.GET, path, null,
            { response ->
                val reservaArray: JSONArray = response
                var reservas: ArrayList<Reserva> = ArrayList()
                for (i in 0 until reservaArray.length()) {
                    val reservaAr = reservaArray.getJSONObject(i)
                    val id = reservaAr.getInt("id")
                    val id_persona = reservaAr.getInt("id_persona")
                    val dni_persona = reservaAr.getInt("id_persona")
                    val fecha_entrada = reservaAr.getString("fecha_entrada")
                    val fecha_salida = reservaAr.getString("fecha_salida")
                    val localizador_reserva = reservaAr.getString("localizador_reserva")
                    val num_personas = reservaAr.getInt("num_personas")
                    val acompañantes = reservaAr.getString("acompanantes")
                    val num_vehiculos = reservaAr.getInt("num_vehiculos")
                    val num_casetas = reservaAr.getInt("num_casetas")
                    val num_bus = reservaAr.getInt("num_bus")
                    val num_caravanas = reservaAr.getInt("num_caravanas")
                    val matricula = reservaAr.getString("matriculas")
                    val checkin = reservaAr.getString("checkin")
                    val fecha_checkin = reservaAr.getString("fecha_checkin")
                    val incidencia = reservaAr.getString("incidencia")
                    val incidencias = reservaAr.getString("incidencias")
                    val estado = reservaAr.getString("estado")
                    val id_zona = reservaAr.getInt("id_zona")

                    reservas.add(Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona))
                }
                completionHandler(reservas)
            },
            { error ->
                completionHandler(null)
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getBooking(context: Context, zoneId: Int, completionHandler: (response: ArrayList<Reserva>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva_zone/" + zoneId
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
                { response ->
                    var bookings: ArrayList<Reserva> = ArrayList()
                    for (i in 0 until response.length()) {
                        val booking = response.getJSONObject(i)
                        val id = booking.getInt("id")
                        val id_persona = booking.getInt("id_persona")
                        val dni_persona = booking.getInt("id_persona")
                        val fecha_entrada = booking.getString("fecha_entrada")
                        val fecha_salida = booking.getString("fecha_salida")
                        val localizador_reserva = booking.getString("localizador_reserva")
                        val num_personas = booking.getInt("num_personas")
                        val acompañantes = booking.getString("acompanantes")
                        val num_vehiculos = booking.getInt("num_vehiculos")
                        val num_casetas = booking.getInt("num_casetas")
                        val num_bus = booking.getInt("num_bus")
                        val num_caravanas = booking.getInt("num_caravanas")
                        val matricula = booking.getString("matriculas")
                        val checkin = booking.getString("checkin")
                        val fecha_checkin = booking.getString("fecha_checkin")
                        val incidencia = booking.getString("incidencia")
                        val incidencias = booking.getString("incidencias")
                        val estado = booking.getString("estado")
                        val id_zona = booking.getInt("id_zona")
                        bookings.add(Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona))
                    }
                    completionHandler(bookings)
                },
                { error ->
                    completionHandler(ArrayList<Reserva>())
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

    override fun getBookingByDate(context: Context, zoneId: Int, date:String, completionHandler: (response: ArrayList<Reserva>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva_zone/" + zoneId + "/" + date
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
                { response ->
                    var bookings: ArrayList<Reserva> = ArrayList()
                    for (i in 0 until response.length()) {
                        val booking = response.getJSONObject(i)
                        val id = booking.getInt("id")
                        val id_persona = booking.getInt("id_persona")
                        val dni_persona = booking.getInt("id_persona")
                        val fecha_entrada = booking.getString("fecha_entrada")
                        val fecha_salida = booking.getString("fecha_salida")
                        val localizador_reserva = booking.getString("localizador_reserva")
                        val num_personas = booking.getInt("num_personas")
                        val acompañantes = booking.getString("acompanantes")
                        val num_vehiculos = booking.getInt("num_vehiculos")
                        val num_casetas = booking.getInt("num_casetas")
                        val num_bus = booking.getInt("num_bus")
                        val num_caravanas = booking.getInt("num_caravanas")
                        val matricula = booking.getString("matriculas")
                        val checkin = booking.getString("checkin")
                        val fecha_checkin = booking.getString("fecha_checkin")
                        val incidencia = booking.getString("incidencia")
                        val incidencias = booking.getString("incidencias")
                        val estado = booking.getString("estado")
                        val id_zona = booking.getInt("id_zona")
                        bookings.add(Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona))
                    }
                    completionHandler(bookings)
                },
                { error ->
                    completionHandler(ArrayList<Reserva>())
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

    override fun getBookingByDate2(context: Context, date:String, completionHandler: (response: ArrayList<Reserva>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva_zone_date/" + "/" + date
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
            { response ->
                var bookings: ArrayList<Reserva> = ArrayList()
                for (i in 0 until response.length()) {
                    val booking = response.getJSONObject(i)
                    val id = booking.getInt("id")
                    val id_persona = booking.getInt("id_persona")
                    val dni_persona = booking.getInt("id_persona")
                    val fecha_entrada = booking.getString("fecha_entrada")
                    val fecha_salida = booking.getString("fecha_salida")
                    val localizador_reserva = booking.getString("localizador_reserva")
                    val num_personas = booking.getInt("num_personas")
                    val acompañantes = booking.getString("acompanantes")
                    val num_vehiculos = booking.getInt("num_vehiculos")
                    val num_casetas = booking.getInt("num_casetas")
                    val num_bus = booking.getInt("num_bus")
                    val num_caravanas = booking.getInt("num_caravanas")
                    val matricula = booking.getString("matriculas")
                    val checkin = booking.getString("checkin")
                    val fecha_checkin = booking.getString("fecha_checkin")
                    val incidencia = booking.getString("incidencia")
                    val incidencias = booking.getString("incidencias")
                    val estado = booking.getString("estado")
                    val id_zona = booking.getInt("id_zona")
                    bookings.add(Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona))
                }
                completionHandler(bookings)
            },
            { error ->
                completionHandler(ArrayList<Reserva>())
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

    override fun getBookingByLocalizador(context: Context, localizador_id: String, completionHandler: (response: ArrayList<Reserva>?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva_local/" + localizador_id
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
                { response ->
                    var bookings: ArrayList<Reserva> = ArrayList()
                    for (i in 0 until response.length()) {
                        val booking = response.getJSONObject(i)
                        val id = booking.getInt("id")
                        val id_persona = booking.getInt("id_persona")
                        val dni_persona = booking.getInt("id_persona")
                        val fecha_entrada = booking.getString("fecha_entrada")
                        val fecha_salida = booking.getString("fecha_salida")
                        val localizador_reserva = booking.getString("localizador_reserva")
                        val num_personas = booking.getInt("num_personas")
                        val acompañantes = booking.getString("acompanantes")
                        val num_vehiculos = booking.getInt("num_vehiculos")
                        val num_casetas = booking.getInt("num_casetas")
                        val num_bus = booking.getInt("num_bus")
                        val num_caravanas = booking.getInt("num_caravanas")
                        val matricula = booking.getString("matriculas")
                        val checkin = booking.getString("checkin")
                        val fecha_checkin = booking.getString("fecha_checkin")
                        val incidencia = booking.getString("incidencia")
                        val incidencias = booking.getString("incidencias")
                        val estado = booking.getString("estado")
                        val id_zona = booking.getInt("id_zona")
                        bookings.add(Reserva(id, id_persona, "", fecha_entrada, fecha_salida, localizador_reserva, num_personas, acompañantes, num_vehiculos, num_casetas, num_bus, num_caravanas, matricula, checkin, fecha_checkin, incidencia, incidencias, estado, id_zona))
                    }
                    completionHandler(bookings)
                },
                { error ->
                    completionHandler(ArrayList<Reserva>())
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

    override fun createReserve(context: Context, reserva: Reserva, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reservaCreate"
        val bookingJSON: JSONObject = JSONObject()
        bookingJSON.put("id", reserva.id.toString())
        bookingJSON.put("id_persona", reserva.id_persona.toString())
        bookingJSON.put("dni_persona", reserva.dni_persona)
        bookingJSON.put("fecha_entrada", reserva.fecha_entrada)
        bookingJSON.put("fecha_salida", reserva.fecha_salida)
        bookingJSON.put("localizador_reserva", reserva.localizador_reserva)
        bookingJSON.put("num_personas", reserva.num_personas.toString())
        bookingJSON.put("num_vehiculos", reserva.num_vehiculos.toString())
        bookingJSON.put("checkin", reserva.checkin)
        bookingJSON.put("fecha_checkin", reserva.fecha_checkin)
        bookingJSON.put("id_zona", reserva.id_zona.toString())

        val objectRequest = JsonObjectRequest(Request.Method.POST, path, bookingJSON,
                { response -> completionHandler() },
                { error -> completionHandler() })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun updateReserve(context: Context, reserva: Reserva, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reservaUpdate/" + reserva.id
        val bookingJSON: JSONObject = JSONObject()
        bookingJSON.put("id", reserva.id.toString())
        bookingJSON.put("id_persona", reserva.id_persona.toString())
        bookingJSON.put("dni_persona", reserva.dni_persona)
        bookingJSON.put("fecha_entrada", reserva.fecha_entrada)
        bookingJSON.put("fecha_salida", reserva.fecha_salida)
        bookingJSON.put("localizador_reserva", reserva.localizador_reserva)
        bookingJSON.put("num_personas", reserva.num_personas.toString())
        bookingJSON.put("num_vehiculos", reserva.num_vehiculos.toString())
        bookingJSON.put("checkin", reserva.checkin)
        bookingJSON.put("fecha_checkin", reserva.fecha_checkin)
        bookingJSON.put("id_zona", reserva.id_zona.toString())

        val objectRequest = JsonObjectRequest(Request.Method.PUT, path, bookingJSON,
                { response -> completionHandler() },
                { error -> completionHandler() })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    /*----------------------Operarios-------------------------*/
    override fun getOpById(context: Context, id: Int, completionHandler: (response: Operario?) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "operario-id/" + id
        val objectRequest = JsonArrayRequest(Request.Method.GET, path, null,
                { response ->
                    if(response == null) { completionHandler(null) }
                    val requestedPost= response.getJSONObject(0)
                    val id = requestedPost.getInt("id")
                    val email = requestedPost.getString("email")
                    val dni = requestedPost.getString("dni")
                    val nombre = requestedPost.getString("nombre")

                    val operario = Operario(id,email,"aaa",nombre,dni,"aaa")
                    completionHandler(operario)
                },
                { error ->
                    Log.v("holi","Error en getById")
                    completionHandler(null)
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun updateUser(context: Context, operario: Operario, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "update-operario/" + operario.id
        val userJson: JSONObject = JSONObject()
        userJson.put("dni", operario.dni)
        userJson.put("nombre", operario.nombre)
        userJson.put("email", operario.email)

        val objectRequest = JsonObjectRequest(Request.Method.PUT, path, userJson,
                { response ->
                    Log.v("update","Se hizo")
                    completionHandler()
                },
                { error ->
                    completionHandler()
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }
    override fun deleteUser(context: Context, id: Int, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "delete-operario/" + id
        val objectRequest = JsonObjectRequest(Request.Method.DELETE, path, null,
                { response ->
                    Log.v("borro", "se borró")
                    completionHandler()
                },
                { error ->
                    Log.v("borro", "error al borrar")
                    completionHandler()
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    /*------------------Auth Operarios-------------------------*/
    override fun logIn(context: Context, operario: Operario, completionHandler: () -> Unit){
        val path = ServiceSingleton.getInstance(context).baseUrl + "login"
        val operarioJson = JSONObject()
        operarioJson.put("email", operario.email)
        operarioJson.put("password", operario.password)
        operarioJson.put("api_token", operario.api_token)
        val objectRequest = JsonObjectRequest(Request.Method.POST, path, operarioJson,
                { response ->
                    completionHandler()
                    val plus = response?.opt("res")
                    val tokn = response?.opt("api_token").toString()
                    val id = response?.opt("id_operario").toString()
                    if (plus == true) {
                        val intent = Intent(context, ZoneActivity::class.java)
                        intent.putExtra("num",1)
                        intent.putExtra("api_token", tokn)
                        intent.putExtra("opeId",id)
                        context.startActivity(intent)
                    } else {
                        Log.v("loginService", "Login incorrecto")
                    }
                },
                { error ->
                    completionHandler()
                    Log.v("login", "Error en login service")
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun createUser(context: Context, operario: Operario, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "signin"
        val operarioJson: JSONObject = JSONObject()
        operarioJson.put("id", 0)
        operarioJson.put("dni",operario.dni)
        operarioJson.put("nombre",operario.nombre)
        operarioJson.put("email",operario.email)
        operarioJson.put("password",operario.password)

        val objectRequest = JsonObjectRequest(Request.Method.POST, path, operarioJson,
                { response -> completionHandler()
                    val plus = response?.opt("res")
                    if (plus==true){
                        Log.v("AddUser","Creado")
                    }else{
                        Log.v("AddUser", "Check Dni or Email")
                        //Aqui Toast o algo para decir que mire
                    }
                },
                { error -> completionHandler()
                    Log.v("AddUser","Roto")
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun createZone(context: Context, zone: Zone, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "add-zona"
        val zoneJson: JSONObject = JSONObject()
        zoneJson.put("id", 0)
        zoneJson.put("nombre",zone.nombre)
        zoneJson.put("localizacion",zone.localizacion)
        zoneJson.put("url_img",zone.url_img)

        val objectRequest = JsonObjectRequest(Request.Method.POST, path, zoneJson,
                { response -> completionHandler()
                },
                { error -> completionHandler()
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }
    override fun deleteZone(context: Context, id: Int, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "delete-zona/" + id
        val objectRequest = JsonObjectRequest(Request.Method.DELETE, path, null,
                { response ->
                    Log.v("borro", "se borró")
                    completionHandler()
                },
                { error ->
                    Log.v("borro", "error al borrar")
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun deleteByReservaId(context: Context, reservaId: Int, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reserva/" + reservaId
        val objectRequest = JsonObjectRequest(Request.Method.DELETE, path, null,
                { response ->
                    completionHandler()
                },
                { error ->
                    completionHandler()
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun updateZone(context: Context, zone: Zone, completionHandler: () -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "update-zona/" + zone.id
        val zoneJson: JSONObject = JSONObject()
        zoneJson.put("nombre", zone.nombre)
        zoneJson.put("localizacion", zone.localizacion)
        zoneJson.put("url_img","prueba2")

        val objectRequest = JsonObjectRequest(Request.Method.PUT, path, zoneJson,
                { response ->
                    Log.v("update","Se hizo")
                    completionHandler()
                },
                { error ->
                    completionHandler()
                })
        ServiceSingleton.getInstance(context).addToRequestQueue(objectRequest)
    }

    override fun getReporte(context: Context, completionHandler: (response: String) -> Unit) {
        val path = ServiceSingleton.getInstance(context).baseUrl + "reporteParametros"
        val arrayRequest = JsonArrayRequest(Request.Method.GET, path, null,
            { response ->
                val value = "status: "
                completionHandler(value)
            },
            { error ->
                completionHandler("dasdsa")
            })
        ServiceSingleton.getInstance(context).addToRequestQueue(arrayRequest)
    }

}