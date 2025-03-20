package com.example.udmath.domain.UseCases

import com.example.udmath.domain.model.User
import com.example.udmath.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    //suspend indica que la función fue suspendida, por lo que se ejecutara en un hilo secundario, una corrutina
    suspend operator fun invoke(user: User): Boolean {

        val accounCreated = authRepository.registerUser(user.email, user.password) != null

        return accounCreated

    }

}