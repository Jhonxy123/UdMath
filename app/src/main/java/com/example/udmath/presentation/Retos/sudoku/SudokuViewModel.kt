package com.example.udmath.presentation.Retos.sudoku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.udmath.domain.model.game.sudoku.SudokuBoard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SudokuViewModel @Inject constructor() : ViewModel() {

    private val _sudokuBoard = MutableStateFlow(SudokuBoard.emptyBoard())
    val sudokuBoard: StateFlow<SudokuBoard> = _sudokuBoard

    private val _selectedCell = MutableStateFlow<Pair<Int, Int>?>(null)
    val selectedCell: StateFlow<Pair<Int, Int>?> = _selectedCell

    private val _currentInputMode = MutableStateFlow(InputMode.NUMBER_INPUT)
    val currentInputMode: StateFlow<InputMode> = _currentInputMode

    private val _gameStatus = MutableStateFlow(GameStatus.PLAYING)
    val gameStatus: StateFlow<GameStatus> = _gameStatus

    init {
        generateNewGame()
    }

    fun generateNewGame() {
        viewModelScope.launch {
            _gameStatus.value = GameStatus.PLAYING
            _selectedCell.value = null
            val newBoard = SudokuBoard.generateSudoku()
            _sudokuBoard.value = newBoard
        }
    }

    fun onCellSelected(row: Int, col: Int) {
        val currentBoard = _sudokuBoard.value
        val newSelectedCell = if (_selectedCell.value == Pair(row, col)) null else Pair(row, col)
        _selectedCell.value = newSelectedCell

        _sudokuBoard.value = if (newSelectedCell != null) {
            currentBoard.highlightRelatedCells(newSelectedCell.first, newSelectedCell.second)
        } else {
            currentBoard.clearHighlights()
        }
    }

    fun onNumberInput(number: Int) {
        _selectedCell.value?.let { (row, col) ->
            val currentBoard = _sudokuBoard.value
            val cell = currentBoard.getCell(row, col)

            if (cell.isEditable) {
                val updatedBoard = when (_currentInputMode.value) {
                    InputMode.NUMBER_INPUT -> currentBoard.setCellValue(row, col, if (number == 0) null else number)
                    InputMode.NOTES_INPUT -> currentBoard.toggleNote(row, col, number)
                }
                val boardAfterConflictCheck = updatedBoard.checkConflicts()
                _sudokuBoard.value = boardAfterConflictCheck

                _gameStatus.value = if (boardAfterConflictCheck.isSolved()) {
                    GameStatus.SOLVED
                } else {
                    GameStatus.PLAYING
                }
            }
        }
    }

    fun toggleInputMode() {
        _currentInputMode.value = when (_currentInputMode.value) {
            InputMode.NUMBER_INPUT -> InputMode.NOTES_INPUT
            InputMode.NOTES_INPUT -> InputMode.NUMBER_INPUT
        }
    }
}

enum class InputMode {
    NUMBER_INPUT, NOTES_INPUT
}

enum class GameStatus {
    PLAYING, SOLVED
}