package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incidencias_espec")
class IncidenciaEspec
    (@PrimaryKey(autoGenerate = true)
     val texto: String,
     val fechahora: String,
     val idusuario: String,)
