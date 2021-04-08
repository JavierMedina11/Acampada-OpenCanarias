package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "zonas")
class Zone (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    val localizacion: String,
    val url_img: String)