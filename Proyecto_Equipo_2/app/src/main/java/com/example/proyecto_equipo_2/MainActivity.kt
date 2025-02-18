package com.example.proyecto_equipo_2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar botones
        findViewById<MaterialButton>(R.id.btnIrARegistro).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java)) // Inicia RegisterActivity
        }
    }
}
