package com.example.proyecto_equipo_2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar botones
        findViewById<MaterialButton>(R.id.btnIrARegistro).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java)) // Inicia RegisterActivity
        }
        findViewById<MaterialButton>(R.id.btnIniciarSesion_0).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java)) // Inicia RegisterActivity
        }
    }
}
