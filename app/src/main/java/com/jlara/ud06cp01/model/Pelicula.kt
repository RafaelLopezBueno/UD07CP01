package com.jlara.ud06cp01.model

import java.io.Serializable

data class Pelicula(
    val id: Int,
    var nombre: String,
    val descripcion: String,
    val imagen: Int,
    val duracion: Int,
    val anio: Int,
    val pais: String
) : Serializable
