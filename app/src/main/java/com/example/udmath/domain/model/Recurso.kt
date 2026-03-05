package com.example.udmath.domain.model
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference


data class Recurso(
    val titulo: String = "",
    val tipo: String = "",
    val modulo: String = "",
    val fecha_agregado: Timestamp? = null,
    val descripcion: String = "",
    val autor: String = "",       // nombre visible
    val autorId: String = "",     // ✅ nuevo
    val imagen: String = "",
    val url: String = ""
)


