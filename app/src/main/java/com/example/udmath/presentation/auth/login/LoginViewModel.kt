package com.example.udmath.presentation.auth.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    fun onEmailChanged(email: String){
        _email.value = email
    }
    fun onPasswordChanged(password: String){
        _password.value = password
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) = viewModelScope.launch{
        try{
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{task->
                    if(task.isSuccessful){
                        Log.d("UdMath","signInWithEmailAndPassword Logueado!!")
                        home()
                    }else{
                        Log.d("UdMath","signInWithEmailAndPassword: ${task.result.toString()}")
                    }
                }
        }catch(ex:Exception){
            Log.d("UdMath","signInWithEmailAndPassword: ${ex.message}")
        }
    }
}