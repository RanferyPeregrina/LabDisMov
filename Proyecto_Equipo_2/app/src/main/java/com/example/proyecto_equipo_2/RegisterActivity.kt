package com.example.proyecto_equipo_2

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var etNombreCompleto: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etContrasena: TextInputEditText
    private lateinit var btnRegistrar: MaterialButton
    private lateinit var progressBar: View
    private lateinit var etConfirmarContrasena: TextInputEditText

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inicializar vistas
        etNombreCompleto = findViewById(R.id.etNombreCompleto)
        etCorreo = findViewById(R.id.etCorreo)
        etContrasena = findViewById(R.id.etContrasena)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        progressBar = findViewById(R.id.progressBar)
        etConfirmarContrasena = findViewById(R.id.etConfirmarContrasena)

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        val nombre = etNombreCompleto.text?.toString()?.trim() ?: ""
        val correo = etCorreo.text?.toString()?.trim() ?: ""
        val contrasena = etContrasena.text?.toString()?.trim() ?: ""
        val confirmacion = etConfirmarContrasena.text?.toString()?.trim() ?: ""

        // Validaciones
        if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
            mostrarMensaje("Todos los campos son obligatorios")
            return
        }
        if (contrasena != confirmacion) {
            mostrarMensaje("Las contraseñas no coinciden")
            return
        }

        if (contrasena.length < 6) {
            mostrarMensaje("La contraseña debe tener al menos 6 caracteres")
            return
        }

        mostrarProgreso(true)

        // Registrar en Firebase Auth
        auth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        guardarDatosUsuario(userId, nombre)
                    } else {
                        mostrarProgreso(false)
                        mostrarMensaje("Error al obtener el ID de usuario")
                    }
                } else {
                    mostrarProgreso(false)
                    mostrarMensaje(obtenerMensajeError(task.exception))
                }
            }
    }

    private fun guardarDatosUsuario(userId: String, nombre: String) {
        val userData = hashMapOf(
            "nombre" to nombre,
            "fechaRegistro" to System.currentTimeMillis()
        )

        database.reference.child("usuarios").child(userId).setValue(userData)
            .addOnSuccessListener {
                mostrarProgreso(false)
                mostrarMensaje("Usuario registrado exitosamente")
                finish()
            }
            .addOnFailureListener { e ->
                mostrarProgreso(false)
                mostrarMensaje("Error al guardar datos: ${e.message}")
            }
    }

    private fun obtenerMensajeError(exception: Exception?): String {
        return when (exception?.message) {
            "The email address is badly formatted." -> "El formato del correo electrónico no es válido"
            "The email address is already in use by another account." -> "Este correo ya está registrado"
            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." ->
                "Error de conexión. Verifica tu internet"
            else -> exception?.message ?: "Error desconocido"
        }
    }

    private fun mostrarProgreso(mostrar: Boolean) {
        progressBar.visibility = if (mostrar) View.VISIBLE else View.GONE
        btnRegistrar.isEnabled = !mostrar
    }

    private fun mostrarMensaje(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
