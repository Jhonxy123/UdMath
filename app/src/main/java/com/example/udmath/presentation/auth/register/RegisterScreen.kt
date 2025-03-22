package com.example.udmath.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.udmath.R
import com.example.udmath.domain.model.User
import com.example.udmath.presentation.components.AutoResizedText
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.DarkBlue

@Composable
fun RegisterScreen(viewModel: RegisterViewModel){

    //valores para los campos de texto
    val name by viewModel.name.collectAsStateWithLifecycle()
    val code by viewModel.code.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val ConfirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()

    //valores para el toast
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    LaunchedEffect(toastMessage) { // Se activa cuando cambia toastMessage
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show() // Muestra el Toast
            viewModel.clearToastMessage() // Limpiar el mensaje después de mostrarlo
        }
    }


    Column( modifier= Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ){

        AutoResizedText(imagePainter = painterResource(id = R.drawable.logo_ud), text = "Registro")

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Ingrese por favor los datos solicitados",
            fontSize = 19.sp,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(35.dp))
        NameText(name,{viewModel.onNameChanged(it)}) //Campo de texto para el nombre del usuario
        Spacer(modifier = Modifier.height(48.dp))
        CodeText(code,{viewModel.onCodeChanged(it)}) //Campo de texto para el codigo del usuario
        Spacer(modifier = Modifier.height(48.dp))
        emailText(email,{viewModel.onEmailChanged(it)}) //Campo de texto para el email del usuario
        Spacer(modifier = Modifier.height(48.dp))
        passwordText(password,{viewModel.onPasswordChanged(it)}) //Campo de texto para la contraseña del usuario
        Spacer(modifier = Modifier.height(48.dp))
        ConfirmPasswordText(ConfirmPassword,{viewModel.onConfirmPasswordChanged(it)}) //Campo de texto para la contraseña del usuario
        Spacer(modifier = Modifier.height(48.dp))


        Button(onClick = {
            //val user = User(name, code, email, password) // Crea un objeto User con los datos ingresados, pero genera problemas, ¿problemas con la recomposición?
            viewModel.onRegister(
                User(name, code, email, password)
            )
        },
            modifier = Modifier.width(280.dp).height(50.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Registrarse")
        }

    }

}

// ¿SE PODRA REUTILIZAR UN MISMO COMPONENTE PARA LOS CAMPOS DE TEXTO?

@Composable
fun NameText(name: String, onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = name,
        onValueChange = {newText ->
                            // Validación: solo permite letras y espacios
                            if (newText.matches(Regex("^[A-Za-z ]*$"))) {
                                onTextChanged(newText) // Solo actualiza si el texto es válido
                            }
                        },
        label = { Text("Nombre") },
        isError = !name.matches(Regex("^[A-Za-z ]+\$")), // Muestra error si el texto no es válido
        singleLine = true,
        textStyle = TextStyle(Color.Black)
    )
}

@Composable
fun CodeText(code: String, onTextChanged: (String) -> Unit){
    TextField(
        value = code,
        onValueChange = {onTextChanged(it)},
        label = { Text("Código") }
    )
}

@Composable
fun emailText(email: String, onTextChanged: (String) -> Unit){
    TextField(
        value = email,
        onValueChange = {onTextChanged(it)},
        label = { Text("Correo") }
    )
}

@Composable
fun passwordText(password: String, onTextChanged: (String) -> Unit){
    TextField(
        value = password,
        onValueChange = {onTextChanged(it)},
        label = { Text("Contraseña") }
    )
}

@Composable
fun ConfirmPasswordText(password: String, onTextChanged: (String) -> Unit){
    TextField(
        value = password,
        onValueChange = {onTextChanged(it)},
        label = { Text("Confirmar contraseña") }
    )
}



