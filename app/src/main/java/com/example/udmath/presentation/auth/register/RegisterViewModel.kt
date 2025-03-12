package com.example.udmath.presentation.auth.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class RegisterViewModel: ViewModel(){

    private val _name = MutableStateFlow<String>("")
    val name: StateFlow<String> = _name

    private val _code = MutableStateFlow<String>("")
    val code: StateFlow<String> = _code

    fun onNameChanged(name: String){
        _name.value = name
    }

}
