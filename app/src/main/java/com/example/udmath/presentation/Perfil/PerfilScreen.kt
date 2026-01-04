// presentation/profile/ProfileScreen.kt
package com.example.udmath.presentation.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel,
    navigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    val blueGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF3980C2),
            Color(0xFF184998)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(blueGradient),
        contentAlignment = Alignment.Center
    ) {

        if (state.isLoading) {
            CircularProgressIndicator()
            return@Box
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(
                Modifier.fillMaxWidth().
                height(80.dp)
            )

            // Título
            Card(
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(horizontal = 24.dp),
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0x1AFFFFFF))
                        .padding(horizontal = 22.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "Perfil del Usuario",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(50.dp))

            // Foto circular
            Box(
                modifier = Modifier
                    .size(170.dp)
                    .clip(CircleShape)
                    .background(Color(0x22000000)),
                contentAlignment = Alignment.Center
            ) {
                if (state.photoUrl != null) {
                    AsyncImage(
                        model = state.photoUrl,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                } else {
                    // círculo vacío si no hay foto
                }
            }


            val picker = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent()
            ) { uri ->
                //if (uri != null) viewModel.uploadProfileImage(uri)
            }

            Button(onClick = { picker.launch("image/*") }) {
                Text("Cambiar foto")
            }




            Spacer(Modifier.height(100.dp))

            // Card blanca con info
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileRow(label = "Correo:", value = state.email)
                    ProfileRow(label = "Nombre:", value = state.name)
                    ProfileRow(label = "Código:", value = state.code)

                    if (state.error != null) {
                        Spacer(Modifier.height(6.dp))
                        Text(text = state.error!!, color = Color.Red)
                    }
                }
            }

            Spacer(Modifier.height(100.dp))

            Button(onClick = {
                navigateBack()
            }) {
                Text(text = "Volver")
            }


        }
    }
}

@Composable
private fun ProfileRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(10.dp))
        Text(text = if (value.isBlank()) "-" else value)
    }
}
