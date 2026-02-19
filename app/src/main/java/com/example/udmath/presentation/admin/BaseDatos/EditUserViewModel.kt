package com.example.udmath.presentation.admin.BaseDatos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun load(userId: String) {
        viewModelScope.launch {
            _loading.value = true
            val u = adminRepository.getUser(userId)
            _name.value = u.name
            _email.value = u.email
            _loading.value = false
        }
    }

    fun onNameChange(v: String) { _name.value = v }
    fun onEmailChange(v: String) { _email.value = v }

    fun save(userId: String, onDone: () -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            adminRepository.updateUser(userId, _name.value, _email.value)
            _loading.value = false
            onDone()
        }
    }
}
