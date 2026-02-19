package com.example.udmath.domain.model



// data class
//clase para alacenar y representar datos del usuario
data class User(
    val id: String = "",
    val name: String ="",
    val code: String ="",
    val email: String = "",
    val photoUrl: String? = null,
    // 👇 nuevo campo
    val role: String = "normal"  // "normal" o "admin"
)