package com.example.udmath.domain.model

import com.example.udmath.presentation.admin.Graficas.BarPoint

data class MateriaDashboard(
    val materiaId: String,
    val puntos: List<BarPoint> // label=semestre, aprobado, desaprobado
)