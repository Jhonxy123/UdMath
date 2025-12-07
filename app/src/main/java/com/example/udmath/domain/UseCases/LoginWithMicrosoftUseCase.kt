package com.example.udmath.domain.UseCases

import android.app.Activity
import com.example.udmath.domain.model.User
import com.example.udmath.domain.repository.AuthRepository

// domain/usecases/LoginWithMicrosoftUseCase.kt
class LoginWithMicrosoftUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(activity: Activity): Result<User> {
        return repository.loginWithMicrosoft(activity)
    }
}
