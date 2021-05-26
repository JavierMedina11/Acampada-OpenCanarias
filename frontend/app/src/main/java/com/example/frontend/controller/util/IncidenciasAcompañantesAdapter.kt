package com.example.frontend.controller.util

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.ui.IncidenciasListActivity
import com.example.frontend.controller.util.PreferenceHelper.set


class IncidenciasAcompañantesAdapter(var acompañantesList: ArrayList<Persona>, val context: Context): RecyclerView.Adapter<IncidenciasAcompañantesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_container_acompanantes,
            parent,
            false
        )
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: IncidenciasAcompañantesAdapter.ViewHolder, position: Int) {
        holder.bindView(acompañantesList[position], context)
    }

    override fun getItemCount(): Int {
        return acompañantesList.size;
    }

    fun setNotes(acompañantes: ArrayList<Persona>){
        this.acompañantesList = acompañantes
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindView(persona: Persona, context: Context) {
            val entrada: TextView = itemView.findViewById(R.id.acompañanteName)
            val salida: TextView = itemView.findViewById(R.id.acompañanteDNI)
            val entrada2: ImageButton = itemView.findViewById(R.id.buttonCheck2)
            val salida2: ImageButton = itemView.findViewById(R.id.buttonNoCheck2)
            val salida3: ImageView = itemView.findViewById(R.id.kbvLocationRRR)
            val name = persona.nombre +" "+ persona.apellido1 +" "+ persona.apellido2

            entrada.text = name
            salida.text = persona.dni

            salida2.setOnClickListener{
                entrada.setTextColor(Color.WHITE)
                salida.setTextColor(Color.WHITE)
                entrada2.setBackgroundResource(R.drawable.cheque2)
                salida3.setBackgroundResource(R.drawable.incidencia)

                val preferences = PreferenceHelper.defaultPrefs(context)
                preferences["acompañante_selected_nombre"] = persona.nombre
                preferences["acompañante_selected_apellido1"] = persona.apellido1
                preferences["acompañante_selected_apellido2"] = persona.apellido2

                IncidenciasListActivity().modalListener(preferences, context)
            }

            itemView.setOnClickListener {
                Log.v("dadas", "dddddddddddd")
                Log.v("dadas", persona.dni)
            }

        }

    }
}


