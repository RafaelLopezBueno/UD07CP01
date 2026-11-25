package com.jlara.ud06cp01

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.jlara.ud06cp01.model.Pelicula

class DetalleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)
        aplicarInsets(findViewById(R.id.raiz))

        @Suppress("DEPRECATION")
        val pelicula = intent.getSerializableExtra(EXTRA_PELICULA) as? Pelicula
        if (pelicula == null) {
            finish()
            return
        }

        findViewById<TextView>(R.id.tvTitulo).text = pelicula.nombre
        findViewById<ImageView>(R.id.ivDetallePoster).setImageResource(pelicula.imagen)
        findViewById<TextView>(R.id.tvDescripcion).text =
            getString(R.string.descripcion_fmt, pelicula.descripcion)
        findViewById<TextView>(R.id.tvAnio).text = getString(R.string.anio_fmt, pelicula.anio)
        findViewById<TextView>(R.id.tvDuracion).text =
            getString(R.string.duracion_fmt, pelicula.duracion)
        findViewById<TextView>(R.id.tvPais).text = getString(R.string.pais_fmt, pelicula.pais)
    }

    companion object {
        const val EXTRA_PELICULA = "pelicula"
    }
}
