package com.example.frontend.controller.util

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.frontend.R
import com.example.frontend.controller.ui.ListActivity
import com.example.frontend.controller.ui.UserProfileActivity
import kotlinx.android.synthetic.main.activity_selection.*

class SelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        listeners()
    }

    private fun listeners(){
        val zoneId = this.intent.getIntExtra("zoneId", 1)
        Log.v("IDReocgido", zoneId.toString())

        reservasBtn.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("zoneId", zoneId)
            intent.putExtra("check", 0)
            startActivity(intent)
        }

        salidasBtn.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("zoneId", zoneId)
            intent.putExtra("check", 1)
            startActivity(intent)
        }
    }
}