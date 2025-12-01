// components/TextFields.kt
package com.example.udmath.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.udmath.ui.theme.Blue   // ajusta si tu color se llama distinto

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

