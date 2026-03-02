package com.example.udmath.domain.model

data class MateriaStats(
    val materiaId: String,
    val semestre: String,
    val aprobados: Int,
    val perdidos: Int,
    val total: Int
)