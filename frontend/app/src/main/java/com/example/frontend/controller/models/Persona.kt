package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personas")
class Persona (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val apellido1: String,
    val apellido2: String,
    val tipo_documento: String,
    val dni: String,
    val fecha_nacimiento:String,
    val mail: String,
    val direccion: String,
    val telefono: String,
    val url_img: String)