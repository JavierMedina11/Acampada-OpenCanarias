package com.example.frontend.controller.util

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.ui.ListActivity
import com.squareup.picasso.Picasso

class ZoneAdapter(var zoneLists: ArrayList<Zone>, val context: Context): RecyclerView.Adapter<ZoneAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_container_zones, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(zoneLists[position], context)
    }

    override fun getItemCount(): Int {
        return zoneLists.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(b: Zone, context: Context){
            val url = "https://cryptic-dawn-95434.herokuapp.com/img/"
            val kbvLocation: ImageView = itemView.findViewById(R.id.kbvLocation)
            val textName: TextView = itemView.findViewById(R.id.textTitle)
            val textSubname: TextView = itemView.findViewById(R.id.textLocation)

            val imageUrl = url + b.url_img + ".jpg"
            Picasso.with(context).load(imageUrl).into(kbvLocation);

            textName.text = b.nombre
            textSubname.text = b.localizacion

            itemView.setOnClickListener {
                val intent = Intent(context, ListActivity::class.java)
                intent.putExtra("zoneId", b.id)
                intent.putExtra("nombre", b.nombre)
                intent.putExtra("localizacion", b.localizacion)
                intent.putExtra("state", "Showing")
                context.startActivity(intent)
            }
        }
    }

}