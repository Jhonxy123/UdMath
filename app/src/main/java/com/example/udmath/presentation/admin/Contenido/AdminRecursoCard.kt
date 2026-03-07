package com.example.udmath.presentation.admin.Contenido

import android.content.Intent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.udmath.domain.model.Recurso

@Composable
fun AdminRecursoCard(
    recurso: Recurso,
    onEditarClick: (Recurso) -> Unit,
    onEliminarClick: (Recurso) -> Unit
) {
    var expanded by rememberSaveable(recurso.id) { mutableStateOf(false) }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { expanded = !expanded }
            .animateContentSize()
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = recurso.titulo,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF184998)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = recurso.descripcion,
                color = Color.DarkGray,
                maxLines = if (expanded) Int.MAX_VALUE else 2,
                overflow = TextOverflow.Ellipsis
            )

            if (expanded) {
                if (recurso.imagen.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = recurso.imagen,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 140.dp, max = 240.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Fit
                    )
                }

                if (recurso.url.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ver recurso",
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, recurso.url.toUri())
                            context.startActivity(intent)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                val meta = buildList {
                    if (recurso.autor.isNotBlank()) add("Autor: ${recurso.autor}")
                    if (recurso.tipo.isNotBlank()) add("Tipo: ${recurso.tipo}")
                    if (recurso.modulo.isNotBlank()) add("Módulo: ${recurso.modulo}")
                }.joinToString(" • ")

                if (meta.isNotBlank()) {
                    Text(
                        text = meta,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TextButton(onClick = { onEditarClick(recurso) }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = Color(0xFF184998)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar")
                    }

                    TextButton(onClick = { onEliminarClick(recurso) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Eliminar", color = Color.Red)
                    }
                }
            }
        }

        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = if (expanded) "Contraer" else "Expandir",
            tint = Color(0xFF184998),
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}