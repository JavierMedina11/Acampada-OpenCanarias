package com.example.frontend.controller.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Reserva
import com.example.frontend.controller.ui.ReservaDetalladaActivity
import com.squareup.picasso.Picasso

class ReservaAdapter(var reservaList: ArrayList<Reserva>, val context: Context): RecyclerView.Adapter<ReservaAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.item_container_reservas,
            parent,
            false
        )
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setAnimation(
            AnimationUtils.loadAnimation(
                context,
                R.anim.item_animation_from_right
            )
        )
        holder.bindView(reservaList[position], context)
    }

    override fun getItemCount(): Int {
        return reservaList.size;
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @RequiresApi(Build.VERSION_CODES.M)
        @SuppressLint("ResourceAsColor")
        fun bindView(b: Reserva, context: Context){
            val roomServiceImpl = ServiceImpl()

            val entrada: TextView = itemView.findViewById(R.id.textEntrada)
            val salida: TextView = itemView.findViewById(R.id.textEntrada2)
            val apellidos: TextView = itemView.findViewById(R.id.textEntrada3)
            val nombre: TextView = itemView.findViewById(R.id.textEntrada4)
            val separador: TextView = itemView.findViewById(R.id.textSeparador)
            val dni: TextView = itemView.findViewById(R.id.textDni)
            val kbv: ImageView = itemView.findViewById(R.id.kbvLocation)
            val person: ImageView = itemView.findViewById(R.id.imagePerson)

            entrada.text = b.fecha_entrada
            salida.text = b.fecha_salida

            if(b.checkin == "1"){
                entrada.setTextColor(Color.WHITE)
                salida.setTextColor(Color.WHITE)
                dni.setTextColor(Color.WHITE)
                nombre.setTextColor(Color.WHITE)
                apellidos.setTextColor(Color.WHITE)
                separador.setTextColor(Color.WHITE)
                kbv.setBackgroundResource(R.drawable.check_in)
                person.setForeground(Drawable.createFromPath("@drawable/rounded_image_view2"))
            }else{
                entrada.setTextColor(Color.BLACK)
                salida.setTextColor(Color.BLACK)
                dni.setTextColor(Color.BLACK)
                nombre.setTextColor(Color.BLACK)
                apellidos.setTextColor(Color.BLACK)
                separador.setTextColor(Color.BLACK)
                kbv.setBackgroundResource(R.color.white)
            }

            roomServiceImpl.getPersonById(context, b.id_persona) { response ->
                run {
                    if (response != null) {
                        val url = "https://cryptic-dawn-95434.herokuapp.com/img/"
                        val name: TextView = itemView.findViewById(R.id.textEntrada3)
                        val apellidos: TextView = itemView.findViewById(R.id.textEntrada4)
                        val dni: TextView = itemView.findViewById(R.id.textDni)
                        val imagePerson: ImageView = itemView.findViewById(R.id.imagePerson)

                        name.setText(response?.name ?: "")
                        apellidos.setText(response?.apellidos ?: "")
                        dni.setText(response?.dni ?: "")

                        val imageUrl = url + response.url_img + ".png"
                        Picasso.with(context).load(imageUrl).into(imagePerson);
                    }
                    itemView.setOnClickListener {
                        val intent = Intent(context, ReservaDetalladaActivity::class.java)
                        intent.putExtra("reservaId", b.id)
                        intent.putExtra("localizador", b.localizador_reserva)
                        intent.putExtra("checkin", b.checkin)
                        intent.putExtra("zoneId", b.id_zona)
                        intent.putExtra("personId", response?.id)
                        intent.putExtra("personName", response?.name)
                        intent.putExtra("state", "Showing")
                        context.startActivity(intent)

                    }
                }
            }
        }
    }

}