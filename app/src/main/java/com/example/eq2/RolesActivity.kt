package com.example.eq2

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eq2.databinding.ActivityRolesBinding

class RolesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRolesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRolesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Datos de ejemplo
        val usuarios = listOf("Juan Pérez", "María García", "Carlos López")
        val roles = listOf("Administrador", "Editor", "Usuario")

        // Configurar adaptadores para los spinners
        val usuariosAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, usuarios)
        val rolesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        binding.spinnerUsuarios.adapter = usuariosAdapter
        binding.spinnerRoles.adapter = rolesAdapter

        // Configurar botón de asignar
        binding.btnAsignarRol.setOnClickListener {
            val usuarioSeleccionado = binding.spinnerUsuarios.selectedItem.toString()
            val rolSeleccionado = binding.spinnerRoles.selectedItem.toString()

            Toast.makeText(
                this,
                "Rol $rolSeleccionado asignado a $usuarioSeleccionado",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Configurar botón de volver
        binding.btnVolver.setOnClickListener {
            finish()
        }
    }
}
