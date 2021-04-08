package com.example.frontend.controller.io

import android.content.Context
import com.example.frontend.controller.models.Operario
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva

import com.example.frontend.controller.models.Zone

interface IVolleyService {

    fun getAll(context: Context,token: String, completionHandler: (response: ArrayList<Zone>?) -> Unit)

    fun getReporte(context: Context, completionHandler: (response: String) -> Unit)

    fun getZoneById(context: Context, zoneId: Int, completionHandler: (response: Zone?) -> Unit)

    fun getBookingById(context: Context, zoneId: Int, completionHandler: (response: Reserva?) -> Unit)

    fun getPersonById(context: Context, zoneId: Int, completionHandler: (response: Persona?) -> Unit)

    fun getBooking(context: Context, userId: Int, completionHandler: (response: ArrayList<Reserva>?) -> Unit)

    fun getBookingByDate(context: Context, userId: Int, date:String, completionHandler: (response: ArrayList<Reserva>?) -> Unit)

    fun getBookingByLocalizador(context: Context, localizador_id: String, completionHandler: (response: ArrayList<Reserva>?) -> Unit)

    fun createReserve(context: Context, reserva: Reserva, completionHandler: () -> Unit)

    fun updateReserve(context: Context,  reserva: Reserva, completionHandler: () -> Unit)

    fun getOpById(context: Context, id: Int, completionHandler: (response: Operario?) -> Unit)

    fun updateUser(context: Context, operario: Operario, completionHandler: () -> Unit)

    fun deleteUser(context: Context, id:Int, completionHandler: () -> Unit)

    fun logIn(context: Context, operario: Operario, completionHandler: () -> Unit)

    fun createUser(context: Context, operario: Operario, completionHandler: () -> Unit)

    fun deleteByReservaId(context: Context, reservaId: Int, completionHandler: () -> Unit)

    fun createZone(context: Context, zone: Zone, completionHandler: () -> Unit)

    fun deleteZone(context: Context, id:Int, completionHandler: () -> Unit)

    fun updateZone(context: Context, zone: Zone, completionHandler: () -> Unit)

}