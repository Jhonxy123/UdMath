package com.example.udmath.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.UseCases.RegisterUserUseCase
import com.example.udmath.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel(){

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _code = MutableStateFlow<String>("")
    val code: StateFlow<String> = _code

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    fun onNameChanged(name: String){
        _name.value = name
    }

    fun onPasswordChanged(password: String){
        _password.value = password
    }

    fun onRegister(user: User){
        viewModelScope.launch {
            val result = registerUserUseCase(user) // ✅ Se ejecuta en una corrutina
        }
    }


}
