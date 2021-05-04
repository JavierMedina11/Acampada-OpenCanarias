package com.example.frontend.controller.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.frontend.controller.models.Reserva

@Dao
interface ReservasDao {
    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_entrada = :fecha")
    fun getByDate(zoneId: Int, fecha: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_salida = :fecha")
    fun getByDateSalidas(zoneId: Int, fecha: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_entrada = :fecha OR fecha_entrada = :fecha2 OR fecha_entrada = :fecha3 OR fecha_entrada = :fecha4")
    fun getByDatePer4Days(zoneId: Int, fecha: String, fecha2: String, fecha3: String, fecha4: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_salida = :fecha OR fecha_salida = :fecha2 OR fecha_salida = :fecha3 OR fecha_salida = :fecha4")
    fun getByDatePer4DaySalidas(zoneId: Int, fecha: String, fecha2: String, fecha3: String, fecha4: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_entrada = :fecha OR fecha_entrada = :fecha2 OR fecha_entrada = :fecha3 OR fecha_entrada = :fecha4 OR fecha_entrada = :fecha5 OR fecha_entrada = :fecha6 OR fecha_entrada = :fecha7")
    fun getByDatePer7Days(zoneId: Int, fecha: String, fecha2: String, fecha3: String, fecha4: String, fecha5: String, fecha6: String, fecha7: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId  AND fecha_salida = :fecha OR fecha_salida = :fecha2 OR fecha_salida = :fecha3 OR fecha_salida = :fecha4 OR fecha_salida = :fecha5 OR fecha_entrada = :fecha6 OR fecha_entrada = :fecha7")
    fun getByDatePer7DaySalidas(zoneId: Int, fecha: String, fecha2: String, fecha3: String, fecha4: String, fecha5: String, fecha6: String, fecha7: String): LiveData<List<Reserva>>

    // SON ESTAS DE AQUI ABAJO

    @Query("SELECT * FROM reservas  where id_zona = :zoneId AND fecha_entrada = :fecha  AND checkin =:check")
    fun getByDateChecked(zoneId: Int, check: String, fecha: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId AND checkin =:check AND (fecha_entrada = :fecha OR fecha_entrada = :fecha2 OR fecha_entrada = :fecha3 OR fecha_entrada = :fecha4)")
    fun getByDateCheckedPer4Days(zoneId: Int, check: String, fecha: String, fecha2: String, fecha3: String, fecha4: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId AND checkin =:check AND (fecha_entrada = :fecha OR fecha_entrada = :fecha2 OR fecha_entrada = :fecha3 OR fecha_entrada = :fecha4 OR fecha_entrada = :fecha5 OR fecha_entrada = :fecha6 OR fecha_entrada = :fecha7)")
    fun getByDateCheckedPer7Days(zoneId: Int, check: String, fecha: String, fecha2: String, fecha3: String, fecha4: String, fecha5: String, fecha6: String, fecha7: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId AND fecha_salida = :fecha  AND checkin =:check")
    fun getByDateCheckedSalidas(zoneId: Int, check: String, fecha: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId AND checkin =:check AND (fecha_salida = :fecha OR fecha_salida = :fecha2 OR fecha_salida = :fecha3 OR fecha_salida = :fecha4)")
    fun getByDateCheckedPer4DaySalidas(zoneId: Int, check: String, fecha: String, fecha2: String, fecha3: String, fecha4: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id_zona = :zoneId AND checkin =:check AND (fecha_salida = :fecha OR fecha_salida = :fecha2 OR fecha_salida = :fecha3 OR fecha_salida = :fecha4 OR fecha_salida = :fecha5 OR fecha_entrada = :fecha6 OR fecha_entrada = :fecha7)")
    fun getByDateCheckedPer7DaySalidas(zoneId: Int, check: String, fecha: String, fecha2: String, fecha3: String, fecha4: String, fecha5: String, fecha6: String, fecha7: String): LiveData<List<Reserva>>

    // SON ESTAS DE AQUI ARRIBA

    @Query("SELECT * FROM reservas  where localizador_reserva = :localizador")
    fun getByLocalizador(localizador: String): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas  where id = :reservaId")
    fun getById(reservaId: Int): LiveData<List<Reserva>>

    @Query("SELECT * FROM reservas WHERE checkin = 1")
    fun getByCheck1(): LiveData<List<Reserva>>

    @Query("UPDATE reservas SET checkin = 1  where id = :reservaId")
    fun checkIn(reservaId: Int)

    @Insert()
    fun insert( reserva: Reserva)

    @Update()
    fun update( reserva: Reserva)

    @Delete()
    fun delete( reserva: Reserva)

    @Query("DELETE FROM reservas")
    fun delete()

    @Query("DELETE FROM reservas WHERE checkin =:checkin")
    fun deleteNoChecked(checkin: String)
}



