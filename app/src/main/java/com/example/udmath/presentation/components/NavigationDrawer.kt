package com.example.udmath.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.udmath.R
import com.example.udmath.domain.model.UserUi
import com.example.udmath.ui.theme.white


val blueGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF3980C2),  // Azul claro (parte superior)
        Color(0xFF184998)   // Azul oscuro (parte inferior)
    )
)



@Composable
fun NavigationDrawer(
    user: UserUi?,
    onLogout: () -> Unit = {},  // Función de cierre de sesión callback
    onProfileClicked: () -> Unit = {}
){

    Column(modifier = Modifier.fillMaxSize()
        .background(blueGradient)
        .padding(horizontal = 15.dp, vertical = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.fillMaxWidth().height(25.dp))

        if (user?.photoUrl != null) {
            AsyncImage(
                model = user.photoUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.logo_ud),
                contentDescription = "Logo"
            )
        }

        Spacer(modifier = Modifier.fillMaxWidth().height(15.dp))

        // CORREO DEL USUARIO
        Text(
            text = user?.email ?: "Invitado",
            color = white,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.fillMaxWidth().height(50.dp))

        NavigationDrawerButton("Editar Perfil", Icons.Default.Edit, onclick = { onProfileClicked() })

        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))

        NavigationDrawerButton("Ajustes", Icons.Default.Settings, onclick = {})

        Spacer(modifier = Modifier.fillMaxWidth().height(20.dp))

        NavigationDrawerButton("Progreso", Icons.Default.DateRange, onclick = {})

        // 🔹 Este Spacer empuja el siguiente contenido hacia abajo 👇
        Spacer(modifier = Modifier.weight(1f))

        NavigationDrawerButton("Cerrar Sesión", Icons.Default.ExitToApp, onclick = { onLogout() })

    }

}