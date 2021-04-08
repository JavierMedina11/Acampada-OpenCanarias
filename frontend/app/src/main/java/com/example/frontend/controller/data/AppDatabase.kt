package com.opencanarias.pruebasync.util

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.frontend.controller.data.OperariosDao
import com.example.frontend.controller.data.PersonasDao
import com.example.frontend.controller.data.ReservasDao
import com.example.frontend.controller.models.Operario
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.models.Zone

@Database(entities = [Zone::class, Reserva::class, Operario::class, Persona::class ], version = 1)
abstract class AppDatabase :RoomDatabase() {

    abstract fun zonas(): ZonasDao
    abstract fun reservas(): ReservasDao
    abstract fun operarios(): OperariosDao
    abstract fun personas(): PersonasDao

    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? =null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}