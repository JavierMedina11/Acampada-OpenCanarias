package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

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
    val url_img: String,
    val localizador: Int,
    val estado: String
    ){

    fun getJSON(){
        val jChangeAcompañanteObject2: JSONObject = JSONObject()
        jChangeAcompañanteObject2.put("nombre", nombre)
        jChangeAcompañanteObject2.put("apellido1", apellido1)
        jChangeAcompañanteObject2.put("apellido2", apellido2)
        jChangeAcompañanteObject2.put("dni", dni)
        jChangeAcompañanteObject2.put("url_imagen", url_img)
        jChangeAcompañanteObject2.put("localizador", localizador)
        jChangeAcompañanteObject2.put("estado", estado)
    }

}


