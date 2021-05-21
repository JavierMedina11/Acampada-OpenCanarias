package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.json.JSONObject


@Entity(tableName = "reservas")
data class Reserva(@PrimaryKey(autoGenerate = true)
                   val id: Int,
                   val id_persona: Int,
                   val dni_persona: String,
                   val fecha_entrada: String,
                   val fecha_salida: String,
                   val localizador_reserva: String,
                   val num_personas: Int,
                   val acompanantes: String,
                   val num_vehiculos: Int,
                   val num_casetas: Int,
                   val num_bus: Int,
                   val num_caravanas: Int,
                   val matriculas: String,
                   val checkin: String,
                   val fecha_checkin: String,
                   val incidencia: String,
                   val incidencias: String,
                   val estado: String,
                   val id_zona: Int)