package com.example.frontend.controller.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.frontend.controller.models.Operario
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone

@Dao
interface OperariosDao {
    @Query("SELECT * FROM operarios")
    fun getAll(): LiveData<List<Operario>>

    @Query("SELECT * FROM operarios  WHERE id = :id")
    fun getById(id: Int): LiveData<List<Operario>>

    @Insert
    fun insert(vararg operario: Operario)

    @Update()
    fun update(operario: Operario)
}