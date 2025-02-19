package com.example.proyecto_equipo_2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreoLogin: TextInputEditText
    private lateinit var etContrasenaLogin: TextInputEditText
    private lateinit var btnIniciarSesion: MaterialButton
    private lateinit var tvIntentosRestantes: TextView
    private lateinit var auth: FirebaseAuth // Inicializa Firebase Auth

    private var intentosFallidos = 0
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializa Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Vincula vistas
        etCorreoLogin = findViewById(R.id.etCorreoLogin)
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        tvIntentosRestantes = findViewById(R.id.tvIntentosRestantes)

        tvIntentosRestantes.text = "Intentos restantes: 3"

        btnIniciarSesion.setOnClickListener { validarCredenciales() }
    }

    private fun validarCredenciales() {
        val correo = etCorreoLogin.text.toString().trim()
        val contrasena = etContrasenaLogin.text.toString().trim()

        if (correo.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios")
            return
        }

        mostrarProgreso(true)

        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mostrarMensaje("Sesión iniciada correctamente")
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    val exception = task.exception as? FirebaseAuthException
                    handleFirebaseAuthError(exception)
                }
                mostrarProgreso(false)
            }
    }

    private fun handleFirebaseAuthError(exception: FirebaseAuthException?) {
        intentosFallidos++
        when (exception?.errorCode) {
            "ERROR_WRONG_PASSWORD" -> {
                if (intentosFallidos >= 3) {
                    bloquearBotonTemporalmente()
                } else {
                    val restantes = 3 - intentosFallidos
                    mostrarMensaje("Contraseña incorrecta. Intentos restantes: $restantes")
                    tvIntentosRestantes.text = "Intentos restantes: $restantes"
                }
            }
            "ERROR_USER_NOT_FOUND" -> mostrarMensaje("Usuario no encontrado")
            "ERROR_INVALID_EMAIL" -> mostrarMensaje("Correo electrónico inválido")
            else -> {
                Log.e("FirebaseAuth", "Error de autenticación: ${exception?.message}")
                mostrarMensaje("Error de autenticación. Intenta de nuevo.")
            }
        }
    }

    private fun bloquearBotonTemporalmente() {
        btnIniciarSesion.isEnabled = false
        mostrarMensaje("Cuenta bloqueada por 20 segundos")
        tvIntentosRestantes.text = "Cuenta bloqueada temporalmente"

        handler.postDelayed({
            btnIniciarSesion.isEnabled = true
            intentosFallidos = 0
            tvIntentosRestantes.text = "Intentos restantes: 3"
        }, 20000)
    }

    private fun mostrarProgreso(mostrar: Boolean) {
        btnIniciarSesion.isEnabled = !mostrar
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
