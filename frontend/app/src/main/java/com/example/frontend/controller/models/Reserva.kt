package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "reservas")
data class Reserva
    (@PrimaryKey(autoGenerate = true)
     val id: Int,
     val id_persona: Int,
     val dni: String,
     val fecha_entrada: String,
     val fecha_salida: String,
     val localizador_reserva: String,
     val num_personas: Int,
     val num_vehiculos: Int,
     val checkin: String,
     val fecha_checkin: String,
     val id_zona: Int)