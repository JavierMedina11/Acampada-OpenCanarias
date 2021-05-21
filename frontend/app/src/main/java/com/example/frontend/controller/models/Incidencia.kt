package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidencias")
class Incidencia
    (@PrimaryKey(autoGenerate = true)
     val plazas: String,
    val casetas: String,
    val caravanas: String,
     val solicitante: String,
     val acompañantes: String,
    val observaciones: String,
)
