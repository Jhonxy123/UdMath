package com.example.udmath.presentation.Recomendaciones.Components.PreguntaDAD

data class DragBlankUiState(
    val dropRect: androidx.compose.ui.geometry.Rect? = null, // solo para hit-test (UI)
    val placedAnswer: String? = null,
    val feedback: Boolean? = null, // null/true/false
    val locked: Boolean = false
)
