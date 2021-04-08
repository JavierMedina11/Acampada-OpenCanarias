package com.example.frontend.controller.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone

@Dao
interface PersonasDao {
    @Query("SELECT * FROM personas")
    fun getAll(): LiveData<List<Persona>>

    @Query("SELECT * FROM personas  WHERE id = :id")
    fun getById(id: Int): LiveData<List<Persona>>

    @Insert
    fun insert(vararg persona: Persona)

    @Update()
    fun update(zone: Persona)
}