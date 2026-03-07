package com.example.udmath.domain.model
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference


data class Recurso(
    val id: String = "",
    val titulo: String = "",
    val tipo: String = "",
    val modulo: String = "",
    val descripcion: String = "",
    val autor: String = "",
    val autorId: String = "",
    val imagen: String = "",
    val url: String = "",
    val fecha_agregado: Timestamp? = null
)
