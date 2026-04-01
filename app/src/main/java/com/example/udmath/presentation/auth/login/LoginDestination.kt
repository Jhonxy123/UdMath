package com.example.udmath.presentation.auth.login
sealed class LoginDestination {
    object MAIN : LoginDestination()
    object ADMIN : LoginDestination()
    data class VERIFY_EMAIL(val email: String) : LoginDestination()
}