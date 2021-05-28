package com.example.frontend.controller.util

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.controller.models.Matricula
import com.example.frontend.controller.models.Persona


class IncidenciaMatriculaAdapter(var zoneLists: ArrayList<Matricula>, val context: Context, val listener2: RowClickListener2): RecyclerView.Adapter<IncidenciaMatriculaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_container_matriculas_2, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entrada2: ImageButton = holder.itemView.findViewById(R.id.buttonCheck34)
        val entrada3: ImageButton = holder.itemView.findViewById(R.id.buttonCheck3)
        entrada2.setOnClickListener {
            listener2.onItemClickListener2(zoneLists[position])
        }
        entrada3.setOnClickListener {
            listener2.onItemClickListener4(zoneLists[position])
        }
        holder.bindView(zoneLists[position], context)
    }

    override fun getItemCount(): Int {
        return zoneLists.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(matricula: Matricula, context: Context){
            val entrada: TextView = itemView.findViewById(R.id.textMatricula)
            val entrada2: ImageButton = itemView.findViewById(R.id.buttonCheck34)
            val entrada10: ImageView = itemView.findViewById(R.id.kbvLocation_op)
            entrada.text = matricula.matricula

            entrada2.setOnClickListener {
                Log.v("sss","SSS")
                entrada10.setBackgroundResource(R.drawable.incencia_matricula_i)
            }
/*
            itemView.setOnClickListener {
                Log.v("dadas", "dddddddddddd")
                Log.v("dadas", matricula.matricula)
            }*/
        }
    }

}

interface RowClickListener2{
    fun onItemClickListener2(matriculaEntity: Matricula)
    fun onItemClickListener4(matriculaEntity: Matricula)
}