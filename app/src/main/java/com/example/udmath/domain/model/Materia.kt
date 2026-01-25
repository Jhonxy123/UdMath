package com.example.udmath.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class Materia(
    val id: String = "",
    val tema: String = "",
    val icono: String = "",
    val fecha_creacion: Timestamp? = null,
    val trofeoRef: DocumentReference? = null
)