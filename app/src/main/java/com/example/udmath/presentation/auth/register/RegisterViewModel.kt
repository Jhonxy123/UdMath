package com.example.udmath.presentation.auth.register

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.UseCases.RegisterUserUseCase
import com.example.udmath.domain.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
//inyección de dependecias con hilt
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel(){

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _code = MutableStateFlow<String>("")
    val code: StateFlow<String> = _code

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow<String>("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    fun onNameChanged(name: String){
        if(isValidName(name) || name.isEmpty()){
            _name.value = name
        }else{
            _toastMessage.value = "El nombre solo puede contener letras y espacios"
        }
    }

    fun onCodeChanged(code: String){
        _code.value = code
    }

    fun onEmailChanged(email: String){
        _email.value = email
    }

    fun onPasswordChanged(password: String){
        _password.value = password
    }

    fun onConfirmPasswordChanged(confirmPassword: String){
        _confirmPassword.value = confirmPassword
    }

    //validar nombre
    fun isValidName(name: String): Boolean {
        return name.matches(Regex("^[A-Za-z ]+\$"))
    }

    // Resetea el mensaje de error después de mostrarlo
    fun clearToastMessage() {
        _toastMessage.value = null
    }

    //funcion para validar que el campo de la contraseña y confirmar contraseña sean iguales
    fun validatePassword(): Boolean{
        return password.value == confirmPassword.value
    }


    // funcion para registrar un usuario
    fun onRegister(user: User){

        if(!validatePassword()){

            _toastMessage.value = "La contraseña no coincide"

        }else if (name.value.isEmpty() || code.value.isEmpty() || email.value.isEmpty() || password.value.isEmpty()){

            _toastMessage.value = "Complete todos los campos"

        } else{

            viewModelScope.launch {// live cycle de viewmodel para que no se corte la corrutina
                registerUserUseCase(user) // Se ejecuta en una corrutina
            }

        }


    }




}
