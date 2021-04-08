package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "personas")
class Persona (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name:
    String,
    val apellidos: String,
    val dni: String,
    val url_img: String)