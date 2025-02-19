package com.example.proyecto_equipo_2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreoLogin: TextInputEditText
    private lateinit var etContrasenaLogin: TextInputEditText
    private lateinit var btnIniciarSesion: MaterialButton

    private var intentosFallidos = 0 // Contador de intentos fallidos
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var tvIntentosRestantes: android.widget.TextView // TextView para mostrar intentos

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar vistas
        etCorreoLogin = findViewById(R.id.etCorreoLogin)
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin)
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion)
        tvIntentosRestantes = findViewById(R.id.tvIntentosRestantes)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Configurar clic del botón "Iniciar Sesión"
        btnIniciarSesion.setOnClickListener {
            validarCredenciales()
        }
    }

    private fun validarCredenciales() {
        val correo = etCorreoLogin.text?.toString()?.trim()
        val contrasena = etContrasenaLogin.text?.toString()?.trim()

        // Validar que los campos no estén vacíos
        if (correo.isNullOrEmpty() || contrasena.isNullOrEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios")
            return
        }

        // Mostrar progreso (deshabilitar botón, etc.)
        mostrarProgreso(true)

        // Intentar autenticar al usuario con Firebase
        auth.signInWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Obtener el usuario actual
                    val user = auth.currentUser

                    // Verificar si el correo electrónico está verificado
                    if (user?.isEmailVerified == true) {
                        mostrarMensaje("Sesión iniciada correctamente")
                        startActivity(Intent(this, HomeActivity::class.java)) // Redirigir al Home
                        finish() // Cerrar la actividad de inicio de sesión
                    } else {
                        mostrarMensaje("Por favor, verifica tu correo electrónico.")
                        auth.signOut() // Cerrar la sesión automáticamente
                    }
                } else {
                    // Manejo de intentos fallidos
                    intentosFallidos++
                    if (intentosFallidos >= 3) {
                        bloquearBotonTemporalmente()
                    } else {
                        val intentosRestantes = 3 - intentosFallidos
                        mostrarMensaje("Contraseña incorrecta. Intentos restantes: $intentosRestantes")
                        tvIntentosRestantes.text = "Intentos restantes: $intentosRestantes"
                    }
                }

                // Ocultar progreso (habilitar botón, etc.)
                mostrarProgreso(false)
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
        }, 20000) // 20 segundos
    }

    private fun mostrarProgreso(mostrar: Boolean) {
        btnIniciarSesion.isEnabled = !mostrar
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
