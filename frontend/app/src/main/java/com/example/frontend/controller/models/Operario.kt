package com.example.frontend.controller.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "operarios")
data class Operario
    (@PrimaryKey(autoGenerate = true)
     val id : Int ,
     val email : String,
     val password:String,
     val nombre : String,
     val dni : String,
     val api_token:String)