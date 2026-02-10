package com.example.udmath.domain.model

data class Progreso(
    val puntos: Int = 0,
    val respondidas: Int = 0,
    val total: Int = 0,
    val respuestas: Map<String, String> = emptyMap()
)

