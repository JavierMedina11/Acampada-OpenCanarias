package com.opencanarias.pruebasync.util

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone

@Dao
interface ZonasDao {
    @Query("SELECT * FROM zonas")
   fun getAll(): LiveData<List<Zone>>

    @Query("SELECT * FROM zonas  WHERE id = :id")
    fun getById(id: Int): LiveData<List<Zone>>

    @Insert
    fun insert(vararg zone: Zone)

    @Update()
    fun update(zone: Zone)
}