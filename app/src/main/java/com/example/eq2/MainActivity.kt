package com.example.eq2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.eq2.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Cargar idioma guardado antes de inflar la UI
        LocaleHelper.loadLocale(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón para ir a la pantalla de Iniciar Sesión
        binding.btnLoginActivity.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Botón para ir a la pantalla de Registro
        binding.btnRegisterActivity.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Nuevo botón para gestión de roles
        binding.btnRolesActivity.setOnClickListener {
            val intent = Intent(this, RolesActivity::class.java)
            startActivity(intent)
        }

        // Botón para cambiar a inglés
        binding.btnChangeLanguage.setOnClickListener {
            val currentLanguage = Locale.getDefault().language
            Log.d("LANGUAGE_DEBUG", "Idioma actual: $currentLanguage")

            val newLanguage = if (currentLanguage == "es") "en" else "es"
            Log.d("LANGUAGE_DEBUG", "Nuevo idioma: $newLanguage")

            LocaleHelper.setLocale(this, newLanguage)
            recreate()  // Recargar la actividad para aplicar cambios
        }
    }
}
