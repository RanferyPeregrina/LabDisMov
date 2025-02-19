package com.example.proyecto_equipo_2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Obtén una referencia a Firebase Realtime Database
        val database = FirebaseDatabase.getInstance()
        val testRef = database.getReference("pruebaConexion")

        // Escribir datos de prueba en Firebase
        testRef.setValue("Conexión Exitosa")
            .addOnSuccessListener {
                // Mostrar un mensaje si se escribió correctamente
                Toast.makeText(this, "Conexión Exitosa: Dato guardado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                // Mostrar el error si falló
                Toast.makeText(this, "Error al conectar: ${exception.message}", Toast.LENGTH_SHORT).show()
            }

        // Leer el valor almacenado en Firebase
        testRef.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val value = snapshot.value.toString()
                    Toast.makeText(this, "Valor leído: $value", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "No existe el nodo en Firebase", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al leer datos: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
