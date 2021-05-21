package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidencia")
data class Incidencia (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val idOpe: Int,
    val fecha: String,
    val texto: String)