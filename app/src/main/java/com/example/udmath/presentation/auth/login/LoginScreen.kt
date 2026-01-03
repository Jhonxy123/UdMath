package com.example.udmath.presentation.auth.login

import android.util.Log
import android.R.color.black
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
// import com.example.udmath.presentation.auth.register.emailText
import com.example.udmath.presentation.components.emailText
//  import com.example.udmath.presentation.auth.register.passwordText
import com.example.udmath.presentation.components.passwordText
import com.example.udmath.presentation.components.AutoResizedText
import com.example.udmath.presentation.components.MyBanner
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.DarkBlue
import com.example.udmath.ui.theme.white
import com.example.udmath.ui.theme.Black
import com.google.firebase.auth.FirebaseAuth
import com.example.udmath.R

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    //navigateToRegister: () -> Unit,
    navigateToMenu: () -> Unit,
    navigateBack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            navigateToMenu()
        }
    }
    //val interactionSource = remember { MutableInteractionSource() }
    //val isPressed by interactionSource.collectIsPressedAsState()

    //val elevation: Dp = if (isPressed) 2.dp else 10.dp   // se “hunde”
    //val containerColor = if (isPressed) Color(0xFFF0F0F0) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        /* =======================
           🔵 HEADER SUPERIOR
           ======================= */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 40.dp,
                        bottomEnd = 40.dp
                    )
                )
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF3980C2), // arriba
                            Color(0xFF184998)  // abajo
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painter = painterResource(id = R.drawable.udmath),
                    contentDescription = "UD Math Logo",
                    modifier = Modifier.height(90.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Bienvenido",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        /* =======================
           🟦 TÍTULO
           ======================= */
        Text(
            text = "Iniciar Sesión",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 20.sp,
            color = Color(0xFF184998)
        )

        Spacer(modifier = Modifier.height(28.dp))

        /* =======================
           📧 EMAIL
           ======================= */
        emailLoginText(state.email) {
            viewModel.onEmailChanged(it)
        }

        Spacer(modifier = Modifier.height(18.dp))

        /* =======================
           🔒 PASSWORD
           ======================= */
        passwordLoginText(state.password) {
            viewModel.onPasswordChanged(it)
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* =======================
           🔹 OLVIDÉ CONTRASEÑA
           ======================= */
        TextButton(
            onClick = { /* TODO */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "¿Olvidaste tu contraseña?",
                color = Color(0xFF184998),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        /* =======================
           ❌ ERROR
           ======================= */
        state.errorMessage?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        /* =======================
       🔘 BOTÓN INGRESAR (Mockup)
       ======================= */
        Button(
            onClick = {
                viewModel.login()  /*{
                    navigateToMenu()
                }*/
            },
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .height(50.dp)
                .align(Alignment.CenterHorizontally),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E2E2)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color(0xFF184998),
                disabledContainerColor = Color(0xFFF5F5F5),
                disabledContentColor = Color(0xFF184998).copy(alpha = 0.5f)
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 10.dp,
                pressedElevation = 2.dp,     // 👈 se “hunde” al presionar
                disabledElevation = 0.dp
            )
        ) {
            Text(
                text = if (state.isLoading) "Cargando..." else "Ingresar",
                fontSize = 16.sp,
                color = Color(0xFF184998)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        /* =======================
           🔙 VOLVER
           ======================= */
        TextButton(
            onClick = navigateBack,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Volver",
                color = Blue,      // 👈 mismo azul
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun emailLoginText(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 42.dp),   // ajusta a tu mockup
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ud_math_logo), // tu icono usuario
            contentDescription = null,
            tint = Color(0xFF184998),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = {
                Text(
                    text = "Usuario",
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(18.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE9E9E9),
                unfocusedContainerColor = Color(0xFFE9E9E9),

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

                focusedTextColor = Color(0xFF333333),
                unfocusedTextColor = Color(0xFF333333),
                cursorColor = Color(0xFF184998)
            )
        )
    }
}

@Composable
fun passwordLoginText(
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 42.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.candado), // tu icono candado (usa el correcto)
            contentDescription = null,
            tint = Color(0xFF184998),
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            placeholder = {
                Text(
                    text = "Contraseña",
                    color = Color(0xFFB0B0B0),
                    fontSize = 14.sp
                )
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .clip(RoundedCornerShape(18.dp)),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE9E9E9),
                unfocusedContainerColor = Color(0xFFE9E9E9),

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,

                focusedTextColor = Color(0xFF333333),
                unfocusedTextColor = Color(0xFF333333),
                cursorColor = Color(0xFF184998)
            )
        )
    }
}