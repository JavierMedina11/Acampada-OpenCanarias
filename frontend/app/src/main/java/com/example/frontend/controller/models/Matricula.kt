package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "matriculas")
class Matricula
    (@PrimaryKey(autoGenerate = true)
    val id: Int,
    val matricula: String,
    val tipo: String,
     val localizador: Int,
    val estado: String)
