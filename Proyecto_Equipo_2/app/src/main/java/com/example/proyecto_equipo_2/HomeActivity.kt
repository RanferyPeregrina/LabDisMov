package com.example.proyecto_equipo_2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference

class HomeActivity : AppCompatActivity() {

    // Declaramos una instancia de la base de datos
    private lateinit var database: FirebaseDatabase
    private lateinit var messageRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inicializamos la instancia de la base de datos
        database = FirebaseDatabase.getInstance()

        // Creamos una referencia a un nodo, por ejemplo "message"
        messageRef = database.getReference("message")

        // Ejemplo: escribe un mensaje en la base de datos
        messageRef.setValue("Hello, Firebase!") { error, _ ->
            if (error != null) {
                Toast.makeText(this, "Error al escribir en Firebase: ${error.message}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Mensaje guardado correctamente", Toast.LENGTH_SHORT).show()
            }
        }

        // Si deseas leer el valor del nodo, puedes hacer lo siguiente:
        messageRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val value = snapshot.value.toString()
                Toast.makeText(this, "Valor en Firebase: $value", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No hay datos en Firebase", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error al obtener datos: ${exception.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
