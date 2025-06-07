package com.example.udmath.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.udmath.R
import com.example.udmath.ui.theme.Blue
import com.example.udmath.ui.theme.DarkBlue
import com.example.udmath.ui.theme.FocusTimerYTTheme

@Composable
fun AutoResizedText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge,
    imagePainter: Painter
) {
    var timeTextStyle by remember { mutableStateOf(textStyle) }
    val fontSixeFactor = 0.95f

    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(
            (Brush.verticalGradient(
                colors = listOf(Blue, DarkBlue), startY = 0f, endY = 300f
            ))
        ).padding(WindowInsets.statusBars.asPaddingValues())
    ){

        Spacer(modifier = Modifier.width(20.dp))

        Image(
            painter = imagePainter,
            contentDescription = "Imagen asociada",
            modifier = modifier
                .size(80.dp)
                .clip(CircleShape)
        )

        Text(
            text = text,
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp),
            softWrap = false,
            style = timeTextStyle.copy(color = Color.White),
            textAlign = TextAlign.Center,
            onTextLayout = { result ->
                if (result.didOverflowWidth) {
                    timeTextStyle = timeTextStyle.copy(
                        fontSize = timeTextStyle.fontSize * fontSixeFactor
                    )
                }
            }
        )
    }

}

// 1- Preview annotation
@Preview(
    name = "AutoResizedText",
    showBackground = true
)
// 2- Composable for the preview
@Composable
fun AutoResizedTextPreview(){
// 3-Theme
    FocusTimerYTTheme{
        AutoResizedText(
            imagePainter = painterResource(id = R.drawable.logo_ud),
            text = "Registro"
        )
    }
}

