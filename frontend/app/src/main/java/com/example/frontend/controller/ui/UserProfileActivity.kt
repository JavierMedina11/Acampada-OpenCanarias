package com.example.frontend.controller.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Operario
import com.example.frontend.controller.models.Persona
import com.example.frontend.controller.util.PreferenceHelper
import com.opencanarias.pruebasync.util.AppDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_reserva_detallada.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class UserProfileActivity : AppCompatActivity() {

    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        val id = preferences.getInt("opeId", 1)
        //getById(id)
        listeners()

        var listaOperario = emptyList<Operario>()

        val database = AppDatabase.getDatabase(this)
        database.operarios().getById(1).observe(this, Observer{
            listaOperario = it
            textViewNameUser.text = listaOperario[0].nombre
            textViewInfoDni.text = listaOperario[0].dni
            textViewInfoEmail.text = listaOperario[0].email
        })

    }

    private fun listeners() {
        buttonToReport.setOnClickListener {
            val intent = Intent(this, PDFView::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
/*
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }*/
    }
/*
    private fun getById(id: Int) {
        Log.v("Getbyid", "Estoy aqui: " + id)
        val ServiceImpl = ServiceImpl()
        ServiceImpl.getOpById(this, id) { response ->
            run {
                val nombre: TextView = findViewById(R.id.textViewNameUser)
                val email: TextView = findViewById(R.id.textViewInfoEmail)
                val dni: TextView = findViewById(R.id.textViewInfoDni)

                nombre.setText(response?.nombre ?: "a")
                email.setText(response?.email ?: "a")
                dni.setText(response?.dni ?: "a")
            }
        }
    }*/
}
