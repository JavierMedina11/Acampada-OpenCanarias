package com.example.frontend.controller.ui

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.frontend.R
import kotlinx.android.synthetic.main.activity_incidencias_list.*

class IncidenciasListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incidencias_list)

        textSolicitante.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")
        textAcompañantes.typeface = Typeface.createFromAsset(assets, "fonts/ocra_exp.TTF")


    }



}