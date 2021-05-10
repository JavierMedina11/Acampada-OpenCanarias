package com.example.frontend.controller.util

import android.app.AlertDialog
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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.controller.models.Zone
import com.example.frontend.controller.ui.ListActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_zone.*

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

            val dialogs: Dialog = Dialog(context)
            dialogs.setContentView(R.layout.another_other_view)

            itemView.setOnClickListener {
                //Toast.makeText(context, "Test Click", Toast.LENGTH_SHORT).show()
                dialogs.show()

                val button_popupr: ImageButton = dialogs.findViewById(R.id.button_popupr)
                val button_popuprrr: ImageButton = dialogs.findViewById(R.id.button_popuprrr)
                val button_popu_close: ImageButton = dialogs.findViewById(R.id.buttonClosePopUpS)

                button_popupr.setOnClickListener(){
                    val intent = Intent(context, ListActivity::class.java)
                    intent.putExtra("zoneId", b.id)
                    intent.putExtra("nombre", b.nombre)
                    intent.putExtra("localizacion", b.localizacion)
                    intent.putExtra("state", "Entradas")
                    Log.v("PRueba", "Entradas")
                    context.startActivity(intent)
                }

                button_popuprrr.setOnClickListener(){
                    val intent = Intent(context, ListActivity::class.java)
                    intent.putExtra("zoneId", b.id)
                    intent.putExtra("nombre", b.nombre)
                    intent.putExtra("localizacion", b.localizacion)
                    intent.putExtra("state", "Salidas")
                    Log.v("PRueba", "Salidas")
                    context.startActivity(intent)
                }

                button_popu_close.setOnClickListener(){
                    dialogs.dismiss()
                }



                /*val builder = AlertDialog.Builder(context)
                builder.setTitle("Delete")
                builder.setMessage("Are you sure to delete you account ?")

                Log.v("dadas", "DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD")
                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(
                        context,
                        android.R.string.yes, Toast.LENGTH_SHORT
                    ).show()
                    Log.v("Edit", "borrao")
                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(
                        context,
                        android.R.string.no, Toast.LENGTH_SHORT
                    ).show()
                }
                builder.show()*/
                /*val intent = Intent(context, ListActivity::class.java)
                intent.putExtra("zoneId", b.id)
                intent.putExtra("nombre", b.nombre)
                intent.putExtra("localizacion", b.localizacion)
                intent.putExtra("state", "Showing")
                context.startActivity(intent)*/
            }
        }
    }

}