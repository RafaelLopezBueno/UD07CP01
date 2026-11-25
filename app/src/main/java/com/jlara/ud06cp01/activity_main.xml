package com.jlara.ud06cp01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.jlara.ud06cp01.model.Pelicula

class EditarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar)
        aplicarInsets(findViewById(R.id.raiz))

        @Suppress("DEPRECATION")
        val pelicula = intent.getSerializableExtra(EXTRA_PELICULA) as? Pelicula
        if (pelicula == null) {
            finish()
            return
        }

        val etNombre = findViewById<EditText>(R.id.etNombre)
        findViewById<ImageView>(R.id.ivEditarPoster).setImageResource(pelicula.imagen)
        if (savedInstanceState == null) etNombre.setText(pelicula.nombre)

        findViewById<Button>(R.id.btnCambiar).setOnClickListener {
            val nuevo = etNombre.text.toString().trim()
            if (nuevo.isEmpty()) {
                etNombre.error = getString(R.string.nombre_vacio)
            } else {
                setResult(RESULT_OK, Intent().putExtra(EXTRA_NOMBRE, nuevo))
                finish()
            }
        }
        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    companion object {
        const val EXTRA_PELICULA = "pelicula"
        const val EXTRA_NOMBRE = "nombre"
    }
}
