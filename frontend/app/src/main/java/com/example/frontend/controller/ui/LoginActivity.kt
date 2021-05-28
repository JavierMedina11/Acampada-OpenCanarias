package com.example.frontend.controller.ui

import android.R.attr.password
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.frontend.R
import com.example.frontend.controller.io.ServiceImpl
import com.example.frontend.controller.models.Operario
import com.opencanarias.pruebasync.util.AppDatabase
import kotlinx.android.synthetic.main.activity_login.*



class LoginActivity : AppCompatActivity() {

    override fun onBackPressed(){
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonToZone.setOnClickListener {
            var operarioList = emptyList<Operario>()
            val database = AppDatabase.getDatabase(this)
            database.operarios().getAll().observe(this, Observer {
                operarioList = it
                val pwd = imput2.text.toString().trim() //here i get de pwd in the edittext
                val hash = operarioList[0].password // this is the encrypted pwd
                val result= BCrypt.verifyer().verify(pwd.toCharArray(), hash)
                if (input.text.toString() == operarioList[0].email && result.verified) {
                    val intent = Intent(this, ZoneActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                } else {
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Usuario o contraseña erroneos", Toast.LENGTH_LONG).show()
                }
            })
        }
    }



    private fun goToMainActivity(){
        val intent= Intent(this, ZoneActivity::class.java)
        startActivity(intent)
    }

    private fun logIn(context: Context, operario: Operario){
        val serviceImpl = ServiceImpl()
        serviceImpl.logIn(this, operario)
        {
            run {
                Log.v("LoginActc", "Login creado")
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
            }
        }
    }

    private fun getDataLogin(){
        Log.v("getData", "cqrgado")
        //Aqui añadir el apitoken
        val et_email = findViewById(R.id.input) as EditText
        val et_password = findViewById(R.id.imput2) as EditText
        val btn_submit = findViewById(R.id.buttonToZone) as ImageButton


        // set on-click listener
        btn_submit.setOnClickListener {
            Log.v("getData", "picao")
            val email = et_email.text;
            val password = et_password.text;

            val operario = Operario(0, email.toString(), password.toString(), "", "", "")
            logIn(this, operario)
        }
    }
}

