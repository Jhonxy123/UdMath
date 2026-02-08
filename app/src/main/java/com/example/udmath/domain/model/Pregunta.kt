package com.example.udmath.domain.model

data class Pregunta(
    val id: String = "",
    val texto: String = "",
    val opciones: List<String> = emptyList(),
    val respuestaCorrecta: String = "",
    val puntos: Int = 0
)
