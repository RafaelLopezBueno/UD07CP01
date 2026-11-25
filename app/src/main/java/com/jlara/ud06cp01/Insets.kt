package com.jlara.ud06cp01

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Con edge-to-edge (targetSdk 35+) el contenido queda bajo las barras del sistema y la ActionBar.
 * Aplicamos los insets como padding para que no se solapen.
 */
fun AppCompatActivity.aplicarInsets(vista: View) {
    ViewCompat.setOnApplyWindowInsetsListener(vista) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
        insets
    }
}