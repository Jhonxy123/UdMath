package com.example.udmath.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.udmath.ui.theme.FocusTimerYTTheme

@Composable
fun AutoResizedText(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.displayLarge
){
    var timeTextStyle by remember { mutableStateOf(textStyle) }
    val fontSixeFactor = 0.95
    Text(
        text,
        modifier = modifier.fillMaxWidth().background(Color.Red),
        softWrap = false,
        style = textStyle,
        onTextLayout = { result ->
            if(result.didOverflowWidth){
                timeTextStyle = timeTextStyle.copy(
                    fontSize = timeTextStyle.fontSize * fontSixeFactor
                )
            }
        }
    )
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
            text = "Inicio de sesión"
        )
    }
}

