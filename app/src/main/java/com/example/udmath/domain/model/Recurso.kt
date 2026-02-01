package com.example.udmath.domain.model
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference


data class Recurso(
    val titulo: String = "",
    val tipo: String = "",          // pdf, video, link, etc.
    val modulo: String = "",        // opcional
    val fecha_agregado: Timestamp? = null,
    val descripcion: String = "",
    val autor: String = "",
    val imagen: String = ""
)


/*data class Recurso(
    val titulo: String = "",
    val tipo: String = "",          // pdf, video, link, etc.
    val modulo: String = "",        // opcional
    val url: String = "",

    val autorRef: DocumentReference? = null,
    val materiaRef: DocumentReference? = null,
    val nivelRef: DocumentReference? = null,   // opcional

    val fecha_agregado: Timestamp? = null
)*/
