// components/TextFields.kt
package com.example.udmath.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.ui.theme.Blue   // ajusta si tu color se llama distinto
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

private val fieldBackground = Color(0xFFF1F1F1) // gris clarito

@Composable
fun TextCampName(
    cadena: String,
    onTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "Nombre Completo",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue
        )

        OutlinedTextField(
            value = cadena,
            onValueChange = onTextChanged,
            singleLine = true,
            isError = cadena.isNotEmpty() && !cadena.matches(Regex("^[A-Za-z ]+\$")),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Red,
                focusedContainerColor = fieldBackground,
                unfocusedContainerColor = fieldBackground,
                disabledContainerColor = fieldBackground,
                errorContainerColor = fieldBackground
            )
        )
    }
}

@Composable
fun CodeText(
    code: String,
    onTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Código Estudiantil",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue
        )

        OutlinedTextField(
            value = code,
            onValueChange = onTextChanged,
            singleLine = true,
            isError = code.isNotEmpty() && !code.matches(Regex("\\d+")),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Red,
                focusedContainerColor = fieldBackground,
                unfocusedContainerColor = fieldBackground,
                disabledContainerColor = fieldBackground,
                errorContainerColor = fieldBackground
            )
        )
    }
}

@Composable
fun emailText(
    email: String,
    onTextChanged: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Correo Institucional",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue
        )

        OutlinedTextField(
            value = email,
            onValueChange = onTextChanged,
            singleLine = true,
            isError = email.isNotEmpty() &&
                    !email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")),
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Blue,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Red,
                focusedContainerColor = fieldBackground,
                unfocusedContainerColor = fieldBackground,
                disabledContainerColor = fieldBackground,
                errorContainerColor = fieldBackground
            )
        )
    }
}

@Composable
fun PasswordText(
    password: String,
    onTextChanged: (String) -> Unit,
    showPassword: Boolean,
    onToggleVisibility: () -> Unit,
    isError: Boolean = false,
    texto: String
) {
    val statePassword = remember { TextFieldState() }
    var isFocused by remember { mutableStateOf(false) }

    // VM -> TextFieldState
    LaunchedEffect(password) {
        val current = statePassword.text.toString()
        if (current != password) {
            statePassword.edit { replace(0, length, password) }
        }
    }

    // TextFieldState -> VM
    LaunchedEffect(statePassword) {
        snapshotFlow { statePassword.text.toString() }
            .distinctUntilChanged()
            .collectLatest { text ->
                if (text != password) onTextChanged(text)
            }
    }

    val borderColor = when {
        isError -> Color.Red
        isFocused -> Blue
        else -> Color.Transparent
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = texto,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(fieldBackground, RoundedCornerShape(16.dp))
                .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 16.dp, vertical = 14.dp)
                .onFocusChanged { isFocused = it.isFocused }
        ) {
            BasicSecureTextField(
                state = statePassword,
                textObfuscationMode =
                    if (showPassword) TextObfuscationMode.Visible
                    else TextObfuscationMode.RevealLastTyped,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 40.dp), // espacio para el ícono
                decorator = { innerTextField ->
                    innerTextField()
                }
            )

            Icon(
                imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                contentDescription = "Toggle password visibility",
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .requiredSize(24.dp)
                    .clickable { onToggleVisibility() }
            )
        }
    }
}




@Composable
fun passwordText(
    password: String,
    onTextChanged: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Text(
            text = "Contraseña",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue
        )
    }

    OutlinedTextField(
        value = password,
        onValueChange = onTextChanged,
        singleLine = true,
        textStyle = TextStyle(color = Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Blue,
            unfocusedBorderColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            errorBorderColor = Color.Red,
            focusedContainerColor = fieldBackground,
            unfocusedContainerColor = fieldBackground,
            disabledContainerColor = fieldBackground,
            errorContainerColor = fieldBackground
        )
    )


}


