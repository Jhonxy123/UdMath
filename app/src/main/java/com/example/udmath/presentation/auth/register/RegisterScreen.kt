package com.example.udmath.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.udmath.R
import com.example.udmath.domain.model.User
import com.example.udmath.presentation.components.AutoResizedText
import com.example.udmath.presentation.navigation.Login
import com.example.udmath.ui.theme.DarkBlue
import kotlinx.coroutines.launch


@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController){

    //valores para los campos de texto
    //val name by viewModel.name.collectAsStateWithLifecycle()
    //val code by viewModel.code.collectAsStateWithLifecycle()
    //val email by viewModel.email.collectAsStateWithLifecycle()
    //val password by viewModel.password.collectAsStateWithLifecycle()
    //val ConfirmPassword by viewModel.confirmPassword.collectAsStateWithLifecycle()

    val state by viewModel.state.collectAsStateWithLifecycle()
    val corrutine = rememberCoroutineScope()

    //valores para el toast
    val context = LocalContext.current
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    LaunchedEffect(toastMessage) { // Se activa cuando cambia toastMessage
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show() // Muestra el Toast
            viewModel.clearToastMessage() // Limpiar el mensaje después de mostrarlo
        }
    }

    if(state.isLoading){

        Box(Modifier.fillMaxSize()){
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

    }else{

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
            NameText(state.name,{viewModel.onNameChanged(it)}) //Campo de texto para el nombre del usuario
            Spacer(modifier = Modifier.height(48.dp))
            CodeText(state.code,{viewModel.onCodeChanged(it)}) //Campo de texto para el codigo del usuario
            Spacer(modifier = Modifier.height(48.dp))
            emailText(state.email,{viewModel.onEmailChanged(it)}) //Campo de texto para el email del usuario
            Spacer(modifier = Modifier.height(48.dp))
            passwordText(state.password,{viewModel.onPasswordChanged(it)}) //Campo de texto para la contraseña del usuario
            Spacer(modifier = Modifier.height(48.dp))
            ConfirmPasswordText(state.confirmPassword,{viewModel.onConfirmPasswordChanged(it)}) //Campo de texto para la contraseña del usuario
            Spacer(modifier = Modifier.height(48.dp))

            Button(onClick = {
                //val user = User(name, code, email, password) // Crea un objeto User con los datos ingresados, pero genera problemas, ¿problemas con la recomposición?
                corrutine.launch {
                    if (viewModel.onRegister(User(state.name, state.code, state.email, state.password))){
                        viewModel.onRegisterSelected()
                        navController.navigate(Login)
                    }
                }

            },
            modifier = Modifier.width(280.dp).height(50.dp),
            shape = RoundedCornerShape(16.dp),
            enabled = state.btnEnabled
            ) {
                Text(text = "Registrarse")
            }

        }

    }
}

// ¿SE PODRA REUTILIZAR UN MISMO COMPONENTE PARA LOS CAMPOS DE TEXTO?

@Composable
fun NameText(name: String, onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = name,
        onValueChange = {onTextChanged(it)},
        label = { Text("Nombre") },
        isError = !name.matches(Regex("^[A-Za-z ]+\$")), // Muestra error si el texto no es válido
        singleLine = true,
        textStyle = TextStyle(Color.Black),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun CodeText(code: String, onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = code,
        onValueChange = {onTextChanged(it)},
        label = { Text("Código") },
        isError = !code.matches(Regex("\\d+")), // Muestra error si el texto no es válido
        singleLine = true,
        textStyle = TextStyle(Color.Black),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun emailText(email: String, onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = email,
        onValueChange = {onTextChanged(it)},
        label = { Text("Correo") },
        textStyle = TextStyle(Color.Black),
        isError = !email.matches(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")),// Muestra error si el texto no es válido
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        singleLine = true
    )
}

@Composable
fun passwordText(password: String, onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = password,
        onValueChange = {onTextChanged(it)},
        label = { Text("Contraseña") },
        textStyle = TextStyle(Color.Black),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}

@Composable
fun ConfirmPasswordText(password: String, onTextChanged: (String) -> Unit){
    OutlinedTextField(
        value = password,
        onValueChange = {onTextChanged(it)},
        label = { Text("Confirmar contraseña") },
        textStyle = TextStyle(Color.Black),
        singleLine = true,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    )
}



