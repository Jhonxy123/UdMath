package com.example.udmath.presentation.auth.register

import android.media.metrics.Event
import android.widget.Toast
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.udmath.domain.UseCases.RegisterUserUseCase
import com.example.udmath.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.Pattern
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase
): ViewModel(){

    private val _state = MutableStateFlow(RegisterViewState())
    val state: StateFlow<RegisterViewState> = _state

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage

    fun onNameChanged(name: String){
            _state.update { it.copy(name = name) }
    }

    fun onCodeChanged(code: String) {
        _state.update { it.copy(code = code) }
    }

    fun onEmailChanged(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChanged(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _state.update { it.copy(confirmPassword = confirmPassword) }
    }


    // Resetea el mensaje de error después de mostrarlo
    fun clearToastMessage() {
        _toastMessage.value = null
    }

    //validar nombre
    fun isValidName(): Boolean {
        return _state.value.name.matches(Regex("^[A-Za-z ]+\$"))
    }

    //validar codigo
    fun isValidCode(): Boolean {
        return _state.value.code.matches(Regex("\\d+"))
    }

    //funcion para validar que el campo de la contraseña y confirmar contraseña sean iguales
    fun validatePassword(): Boolean{
        return _state.value.password == _state.value.confirmPassword && _state.value.password.length > 5
    }

    //funcion para validar que el email sea valido
    fun validateEmail(): Boolean{
        return _state.value.email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"))
    }

    suspend fun onRegisterSelected(){
        _state.update { it.copy(isLoading = true) } //actualiza solo la propiedad "isLoading" en el estado sin modificar las demás propiedades
        delay(4000)
        _state.update { it.copy(isLoading = false) }
    }

    // funcion para registrar un usuario
    suspend fun onRegister(user: User): Boolean{

        _state.update { it.copy(btnEnabled = false) }

        return try {
            if(!isValidName()){

                _toastMessage.value = "El nombre solo puede contener letras y espacios"

                false

            }else if(!isValidCode()){

                _toastMessage.value = "El código solo puede contener números"
                false

            }else if(!validatePassword()){

                _toastMessage.value = "La contraseña no coincide, o es muy corta"
                false

            }else if (!validateEmail()){

                _toastMessage.value = "El email no es válido"
                false

            }else if (state.value.name.isEmpty() || state.value.code.isEmpty() || state.value.email.isEmpty() || state.value.password.isEmpty()){

                _toastMessage.value = "Complete todos los campos"
                false

            }else{
                val userCreated = registerUserUseCase(user) // Se ejecuta en una corrutina
                if(userCreated){
                    _toastMessage.value = "Registro exitoso"
                    true
                }else{
                    _toastMessage.value = "Error al registrarse, por favor verifique los datos"
                    false
                }
            }
        }finally {
            _state.update { it.copy(btnEnabled = true) }
        }
    }
}
