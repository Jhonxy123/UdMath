package com.example.udmath.presentation.Retos.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.udmath.domain.model.game.sudoku.Cell
import com.example.udmath.domain.model.game.sudoku.SudokuBoard
import com.example.udmath.presentation.components.TopBarback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SudokuScreen(
    navigateBack: () -> Unit
) {
    val sudokuViewModel: SudokuViewModel = hiltViewModel()
    val sudokuBoard by sudokuViewModel.sudokuBoard.collectAsState()
    val selectedCell by sudokuViewModel.selectedCell.collectAsState()
    val currentInputMode by sudokuViewModel.currentInputMode.collectAsState()
    val gameStatus by sudokuViewModel.gameStatus.collectAsState()

    Text("Estás en Sudoku")
    Scaffold(
        topBar = { TopBarback("Sudoku", navigateBack = {navigateBack()}) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top // <<-- CAMBIO CLAVE AQUÍ: De SpaceAround a Top
            ) {
                if (gameStatus == GameStatus.SOLVED) {
                    Text(
                        "¡Felicidades, has resuelto el Sudoku!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                SudokuBoardUI(
                    board = sudokuBoard,
                    selectedCell = selectedCell,
                    onCellSelected = { row, col -> sudokuViewModel.onCellSelected(row, col) }
                )

                Spacer(Modifier.height(90.dp)) // <<-- ESPACIO REDUCIDO AÚN MÁS (antes 4.dp)

                NumberInputControls(
                    onNumberInput = { number -> sudokuViewModel.onNumberInput(number) },
                    onToggleMode = { sudokuViewModel.toggleInputMode() },
                    onClearCell = { sudokuViewModel.onNumberInput(0) },
                    onNewGame = { sudokuViewModel.generateNewGame() },
                    currentInputMode = currentInputMode
                )
            }
        }
    )
}

@Composable
fun SudokuBoardUI(
    board: SudokuBoard,
    selectedCell: Pair<Int, Int>?,
    onCellSelected: (row: Int, col: Int) -> Unit
) {
    val borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    val thickBorderColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .border(2.dp, thickBorderColor)
    ) {
        for (rowIndex in 0 until 9) {
            Row(
                modifier = Modifier.weight(1f)
            ) {
                for (colIndex in 0 until 9) {
                    val cell = board.getCell(rowIndex, colIndex)
                    SudokuCell(
                        cell = cell,
                        isSelected = selectedCell == Pair(rowIndex, colIndex),
                        onCellSelected = onCellSelected,
                        modifier = Modifier.weight(1f)
                    )
                    if (colIndex % 3 == 2 && colIndex != 8) {
                        Spacer(Modifier.width(2.dp).background(thickBorderColor))
                    }
                }
            }
            if (rowIndex % 3 == 2 && rowIndex != 8) {
                Spacer(Modifier.height(2.dp).fillMaxWidth().background(thickBorderColor))
            }
        }
    }
}

@Composable
fun SudokuCell(
    cell: Cell,
    isSelected: Boolean,
    onCellSelected: (row: Int, col: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.8f)
    val highlightedColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
    val conflictingColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f)
    val defaultCellColor = MaterialTheme.colorScheme.surface
    val editableTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val fixedTextColor = MaterialTheme.colorScheme.onSurface
    val noteTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

    val backgroundColor = when {
        isSelected -> selectedColor
        cell.isHighlighted -> highlightedColor
        else -> defaultCellColor
    }
    val textColor = if (cell.isEditable) editableTextColor else fixedTextColor

    val conflictBorderColor = Color.Red
    val normalBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .border(
                width = if (cell.isConflicting) 2.dp else 0.5.dp,
                color = if (cell.isConflicting) conflictBorderColor else normalBorderColor
            )
            .background(backgroundColor)
            .clickable { onCellSelected(cell.row, cell.col) },
        contentAlignment = Alignment.Center
    ) {
        if (cell.value != null) {
            Text(
                text = cell.value.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (cell.isConflicting) conflictBorderColor else textColor
            )
        } else {
            if (cell.notes.isNotEmpty()) {
                val notesText = cell.notes.sorted().joinToString(" ")
                Text(
                    text = notesText,
                    fontSize = 10.sp,
                    color = noteTextColor,
                    lineHeight = 12.sp,
                    modifier = Modifier.padding(2.dp)
                )
            }
        }
    }
}

@Composable
fun NumberInputControls(
    onNumberInput: (Int) -> Unit,
    onToggleMode: () -> Unit,
    onClearCell: () -> Unit,
    onNewGame: () -> Unit,
    currentInputMode: InputMode
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 0.dp)
    ) {
        for (i in 0 until 3) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 1..3) {
                    val number = i * 3 + j
                    Button(
                        onClick = { onNumberInput(number) },
                        modifier = Modifier
                            .width(90.dp)
                            .height(40.dp)
                            .padding(2.dp)
                            .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                        shape = RoundedCornerShape(0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(number.toString(), fontSize = 18.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onClearCell,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Icon(Icons.Filled.Clear, contentDescription = "Borrar", modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(2.dp))
                Text("Borrar", fontSize = 12.sp)
            }
            /*
            Button(
                onClick = onToggleMode,
                modifier = Modifier
                    .weight(1.5f)
                    .height(40.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(
                    text = if (currentInputMode == InputMode.NUMBER_INPUT) "Notas" else "Num.",
                    fontSize = 12.sp
                )
            }*/

            Button(
                onClick = onNewGame,
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .padding(horizontal = 4.dp, vertical = 2.dp)
                    .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
                shape = RoundedCornerShape(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text("Nuevo", fontSize = 12.sp)
            }
        }
    }
}