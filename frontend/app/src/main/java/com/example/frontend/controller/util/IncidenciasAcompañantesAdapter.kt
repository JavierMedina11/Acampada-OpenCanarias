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


class IncidenciasAcompañantesAdapter(var acompañantesList: ArrayList<Persona>, val context: Context, val listener: RowClickListener): RecyclerView.Adapter<IncidenciasAcompañantesAdapter.ViewHolder>() {

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
        val salida2: ImageButton = holder.itemView.findViewById(R.id.buttonNoCheck2)
        val salida3: ImageView = holder.itemView.findViewById(R.id.kbvLocationRRR)
        salida2.setOnClickListener {
            listener.onItemClickListener(acompañantesList[position])
        }
        salida3.setOnClickListener {
            listener.onItemClickListener3(acompañantesList[position])
        }
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
            val entrada: TextView = itemView.findViewById(R.id.acompananteName)
            val salida: TextView = itemView.findViewById(R.id.acompananteDNI)
            val entrada2: ImageButton = itemView.findViewById(R.id.buttonCheck2)
            val salida2: ImageButton = itemView.findViewById(R.id.buttonNoCheck2)
            val salida3: ImageView = itemView.findViewById(R.id.kbvLocationRRR)
            val name = persona.nombre +" "+ persona.apellido1 +" "+ persona.apellido2

            entrada.text = name
            salida.text = persona.dni
        }
    }
}

interface RowClickListener{
    fun onItemClickListener(acompañanteEntity: Persona)
    fun onItemClickListener3(acompañanteEntity: Persona)
}


