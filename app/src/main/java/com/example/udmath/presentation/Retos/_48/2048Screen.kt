package com.example.udmath.presentation.Retos._48

import android.graphics.Path
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs
import com.example.udmath.domain.model.game._48.Direction
import com.example.udmath.presentation.components.TopBarback



@Composable
fun GameScreen(
    navigateBack: () -> Unit,
    gameViewModel: GameViewModel = viewModel()
) {
    val boardState by gameViewModel.board.observeAsState()
    val board = boardState?.board ?: Array(4) { Array(4) { 0 } }

    var dragStartX by remember { mutableStateOf(0f) }
    var dragStartY by remember { mutableStateOf(0f) }
    var dragOffsetX by remember { mutableStateOf(0f) }
    var dragOffsetY by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBarback("2048", navigateBack = {navigateBack()})
        Spacer(modifier = Modifier.height(18.dp))
//        Text(
//            text = "2048",
//            fontSize = 40.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color(0xFF776E65),
//            modifier = Modifier.padding(bottom = 24.dp)
//        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color(0xFFBBADA0), RoundedCornerShape(8.dp))
                .padding(4.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            dragStartX = offset.x
                            dragStartY = offset.y
                        },
                        onDrag = { change, _ ->
                            dragOffsetX = change.position.x
                            dragOffsetY = change.position.y
                        },
                        onDragEnd = {
                            val diffX = dragOffsetX - dragStartX
                            val diffY = dragOffsetY - dragStartY

                            val direction = when {
                                abs(diffX) > abs(diffY) && abs(diffX) > 10 -> {
                                    if (diffX > 0) Direction.RIGHT else Direction.LEFT
                                }
                                abs(diffY) > abs(diffX) && abs(diffY) > 10 -> {
                                    if (diffY > 0) Direction.DOWN else Direction.UP
                                }
                                else -> null
                            }

                            direction?.let {
                                gameViewModel.move(it)
                            }
                        }
                    )
                }
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                board.forEach { row ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        row.forEach { cell ->
                            Tile(value = cell, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { gameViewModel.resetGame() }) {
            Text(text = "Reiniciar")
        }
    }
}

@Composable
fun Tile(value: Int, modifier: Modifier = Modifier) {
    val backgroundColor = when (value) {
        0 -> Color(0xFFCDC1B4)
        2 -> Color(0xFFEEE4DA)
        4 -> Color(0xFFEDE0C8)
        8 -> Color(0xFFF2B179)
        16 -> Color(0xFFF59563)
        32 -> Color(0xFFF67C5F)
        64 -> Color(0xFFF65E3B)
        128 -> Color(0xFFEDCF72)
        256 -> Color(0xFFEDCC61)
        512 -> Color(0xFFEDC850)
        1024 -> Color(0xFFEDC53F)
        2048 -> Color(0xFFEDC22E)
        else -> Color(0xFF3C3A32)
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(backgroundColor, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            Text(
                text = value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (value <= 4) Color(0xFF776E65) else Color.White
            )
        }
    }
}