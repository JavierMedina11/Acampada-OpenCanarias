package com.example.frontend.controller.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.frontend.controller.models.Reserva

@Dao
interface ReservasDao {
    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_entrada = :fecha")
    fun getByDate(zoneId: Int, fecha: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_entrada = :fecha AND checkin =:check")
    fun getByDateChecked(zoneId: Int, fecha: String, check: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where localizador_reserva = :localizador")
    fun getByLocalizador(localizador: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id = :reservaId")
    fun getById(reservaId: Int): LiveData<List<Reserva>>

    @Query("UPDATE reservas SET checkin = 1  where id = :reservaId")
    fun checkIn(reservaId: Int)

    @Insert()
    fun insert( reserva: Reserva)

    @Update()
    fun update( reserva: Reserva)

    @Delete()
    fun delete( reserva: Reserva)

}



