package com.example.udmath.presentation.auth.register

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.udmath.R
import com.example.udmath.presentation.components.AutoResizedText
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.DarkBlue

@Composable
fun RegisterScreen(viewModel: RegisterViewModel){

    var Correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val name by viewModel.name.collectAsStateWithLifecycle()
    val code by viewModel.code.collectAsStateWithLifecycle()

    Column( modifier= Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
    ){

        AutoResizedText(
            imagePainter = painterResource(id = R.drawable.logo_ud),
            text = "Registro")

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Ingrese por favor los datos solicitados",
            fontSize = 19.sp,
            color = DarkBlue
        )

        Spacer(modifier = Modifier.height(35.dp))

        NameText(name,{viewModel.onNameChanged(it)})

        //Spacer(modifier = Modifier.height(48.dp))

        //CodeText(code,{onCodeChanged(it)})

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = Correo,
            onValueChange = { Correo = it },
            label = { Text("Correo") },
            textStyle = TextStyle(color = DarkBlue)
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            textStyle = TextStyle(color = DarkBlue)
        )

        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Confirmar contraseña") },
            textStyle = TextStyle(color = DarkBlue)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(onClick = {},
            modifier = Modifier.width(280.dp).height(50.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "Navegar al login")
        }


    }

}

@Composable
fun NameText(name: String, onTextChanged: (String) -> Unit){

    TextField(
        value = name,
        onValueChange = {onTextChanged(it)},
        label = { Text("Nombre") }
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
