package com.example.udmath.presentation.admin.BaseDatos

sealed class BdUiEvent {
    data class NavigateToEdit(val userId: String) : BdUiEvent()
    data class ShowMessage(val text: String) : BdUiEvent()
}