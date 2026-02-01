package com.example.udmath.domain.model.game.sudoku

import kotlin.random.Random

data class Cell(
    val row: Int,
    val col: Int,
    val value: Int?, // null para celdas vacías
    val isEditable: Boolean, // false si es un número inicial del puzzle
    val isConflicting: Boolean = false,
    val isHighlighted: Boolean = false,
    val notes: Set<Int> = emptySet()
)

data class SudokuBoard(val cells: List<List<Cell>>) {

    companion object {
        fun emptyBoard(): SudokuBoard {
            return SudokuBoard(List(9) { row ->
                List(9) { col ->
                    Cell(row, col, null, true)
                }
            })
        }

        /**
         * Genera un nuevo tablero de Sudoku seleccionando aleatoriamente
         * uno de los puzzles predefinidos de una lista más amplia.
         */
        fun generateSudoku(): SudokuBoard {
            val initialBoard = MutableList(9) { row ->
                MutableList(9) { col ->
                    Cell(row, col, null, true)
                }
            }

            // Puzzles predefinidos (7 en total)
            val predefinedPuzzles = listOf(
                // Puzzle 1 (Fácil)
                listOf(
                    listOf(5, 3, null, null, 7, null, null, null, null),
                    listOf(6, null, null, 1, 9, 5, null, null, null),
                    listOf(null, 9, 8, null, null, null, null, 6, null),
                    listOf(8, null, null, null, 6, null, null, null, 3),
                    listOf(4, null, null, 8, null, 3, null, null, 1),
                    listOf(7, null, null, null, 2, null, null, null, 6),
                    listOf(null, 6, null, null, null, null, 2, 8, null),
                    listOf(null, null, null, 4, 1, 9, null, null, 5),
                    listOf(null, null, null, null, 8, null, null, 7, 9)
                ),
                // Puzzle 2 (Otro fácil)
                listOf(
                    listOf(null, null, null, 2, 6, null, 7, null, 1),
                    listOf(6, 8, null, null, 7, null, null, 9, null),
                    listOf(1, 9, null, null, null, 4, 5, null, null),
                    listOf(8, 2, null, 1, null, null, null, 4, null),
                    listOf(null, null, 4, 6, null, 2, 9, null, null),
                    listOf(null, 5, null, null, null, 3, null, 2, 8),
                    listOf(null, null, 9, 3, null, null, null, 7, 4),
                    listOf(null, 4, null, null, 5, null, null, 3, 6),
                    listOf(7, null, 3, null, 1, 8, null, null, null)
                ),
                // Puzzle 3 (Nuevo)
                listOf(
                    listOf(null, 7, null, null, null, null, null, null, null),
                    listOf(null, null, null, null, null, null, 4, null, null),
                    listOf(null, null, null, 4, 3, null, 2, null, 7),
                    listOf(null, null, null, 6, null, 7, null, null, null),
                    listOf(5, null, null, null, null, null, null, 3, null),
                    listOf(null, null, null, null, 8, null, null, null, null),
                    listOf(null, null, 4, null, 5, 6, null, null, null),
                    listOf(null, 9, null, null, null, null, null, null, null),
                    listOf(6, null, 3, null, null, null, 8, null, null)
                ),
                // Puzzle 4 (Nuevo)
                listOf(
                    listOf(null, null, null, 7, null, null, null, 6, null),
                    listOf(null, null, null, null, null, null, null, null, 9),
                    listOf(null, 6, 8, null, null, null, 2, null, null),
                    listOf(null, null, 5, null, 9, 7, null, null, null),
                    listOf(null, 8, null, 5, null, 3, null, 9, null),
                    listOf(null, null, null, 1, 8, null, 7, null, null),
                    listOf(null, null, 1, null, null, null, 3, 5, null),
                    listOf(6, null, null, null, null, null, null, null, null),
                    listOf(null, 3, null, null, null, 8, null, null, null)
                ),
                // Puzzle 5 (Nuevo)
                listOf(
                    listOf(null, null, 9, null, 8, null, 4, null, null),
                    listOf(null, 1, null, null, null, 2, null, 5, null),
                    listOf(3, null, 5, null, null, null, null, null, 6),
                    listOf(null, 4, null, null, 1, null, null, 2, null),
                    listOf(null, null, null, 7, null, 9, null, null, null),
                    listOf(null, 7, null, null, 6, null, null, 8, null),
                    listOf(4, null, null, null, null, null, 5, null, 8),
                    listOf(null, 2, null, 3, null, null, null, 9, null),
                    listOf(null, null, 6, null, 7, null, 1, null, null)
                ),
                // Puzzle 6 (Nuevo)
                listOf(
                    listOf(1, null, 3, null, null, 8, null, null, 9),
                    listOf(null, null, null, 2, 7, null, null, 4, null),
                    listOf(null, null, 7, null, null, null, null, null, null),
                    listOf(null, 5, null, null, null, null, 8, null, null),
                    listOf(null, null, null, 9, null, 1, null, null, null),
                    listOf(null, null, 6, null, null, null, null, 2, null),
                    listOf(null, null, null, null, null, null, 9, null, null),
                    listOf(null, 3, null, null, 5, 6, null, null, null),
                    listOf(8, null, null, 7, null, null, 3, null, 1)
                ),
                // Puzzle 7 (Nuevo)
                listOf(
                    listOf(null, null, 6, null, null, null, 1, null, null),
                    listOf(null, 8, null, null, 9, 3, null, 5, null),
                    listOf(9, null, null, null, null, null, null, null, 4),
                    listOf(null, null, 7, null, 1, null, null, null, null),
                    listOf(null, 1, null, 5, null, 4, null, 2, null),
                    listOf(null, null, null, null, 8, null, 7, null, null),
                    listOf(8, null, null, null, null, null, null, null, 2),
                    listOf(null, 2, null, 9, 4, null, null, 1, null),
                    listOf(null, null, 1, null, null, null, 6, null, null)
                )
            )

            // Selecciona un puzzle al azar de la lista
            val selectedPuzzle = predefinedPuzzles[Random.nextInt(predefinedPuzzles.size)]

            for (r in 0 until 9) {
                for (c in 0 until 9) {
                    val value = selectedPuzzle[r][c]
                    initialBoard[r][c] = Cell(r, c, value, isEditable = (value == null))
                }
            }
            return SudokuBoard(initialBoard).checkConflicts().clearHighlights()
        }
    }

    fun getCell(row: Int, col: Int): Cell {
        return cells[row][col]
    }

    fun setCellValue(row: Int, col: Int, value: Int?): SudokuBoard {
        val newCells = cells.toMutableList().map { it.toMutableList() }
        if (newCells[row][col].isEditable) {
            newCells[row][col] = newCells[row][col].copy(value = value, notes = emptySet())
        }
        return SudokuBoard(newCells.map { it.toList() })
    }

    fun toggleNote(row: Int, col: Int, note: Int): SudokuBoard {
        val newCells = cells.toMutableList().map { it.toMutableList() }
        val currentCell = newCells[row][col]
        if (currentCell.isEditable && currentCell.value == null) {
            val newNotes = if (currentCell.notes.contains(note)) {
                currentCell.notes - note
            } else {
                currentCell.notes + note
            }
            newCells[row][col] = currentCell.copy(notes = newNotes.toSet())
        }
        return SudokuBoard(newCells.map { it.toList() })
    }

    fun checkConflicts(): SudokuBoard {
        val updatedCells = cells.toMutableList().map { it.toMutableList() }

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                updatedCells[r][c] = updatedCells[r][c].copy(isConflicting = false)
            }
        }

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                val cell = updatedCells[r][c]
                if (cell.value != null) {
                    val value = cell.value

                    for (k in 0 until 9) {
                        if (k != c && updatedCells[r][k].value == value) {
                            updatedCells[r][c] = updatedCells[r][c].copy(isConflicting = true)
                            updatedCells[r][k] = updatedCells[r][k].copy(isConflicting = true)
                        }
                    }
                    for (k in 0 until 9) {
                        if (k != r && updatedCells[k][c].value == value) {
                            updatedCells[r][c] = updatedCells[r][c].copy(isConflicting = true)
                            updatedCells[k][c] = updatedCells[k][c].copy(isConflicting = true)
                        }
                    }
                    val startRow = (r / 3) * 3
                    val startCol = (c / 3) * 3
                    for (rowBlock in startRow until startRow + 3) {
                        for (colBlock in startCol until startCol + 3) {
                            if ((rowBlock != r || colBlock != c) && updatedCells[rowBlock][colBlock].value == value) {
                                updatedCells[r][c] = updatedCells[r][c].copy(isConflicting = true)
                                updatedCells[rowBlock][colBlock] = updatedCells[rowBlock][colBlock].copy(isConflicting = true)
                            }
                        }
                    }
                }
            }
        }
        return SudokuBoard(updatedCells.map { it.toList() })
    }

    fun isSolved(): Boolean {
        return cells.flatten().all { it.value != null && !it.isConflicting } &&
                (0 until 9).all { isRowValid(it) && isColValid(it) } &&
                (0 until 3).all { br -> (0 until 3).all { bc -> isBlockValid(br * 3, bc * 3) } }
    }

    private fun isRowValid(row: Int): Boolean {
        val values = cells[row].mapNotNull { it.value }
        return values.toSet().size == 9 && values.size == 9
    }

    private fun isColValid(col: Int): Boolean {
        val values = cells.mapNotNull { it[col].value }
        return values.toSet().size == 9 && values.size == 9
    }

    private fun isBlockValid(startRow: Int, startCol: Int): Boolean {
        val values = mutableListOf<Int>()
        for (r in startRow until startRow + 3) {
            for (c in startCol until startCol + 3) {
                cells[r][c].value?.let { values.add(it) }
            }
        }
        return values.toSet().size == 9 && values.size == 9
    }

    fun highlightRelatedCells(row: Int, col: Int): SudokuBoard {
        val newCells = cells.toMutableList().map { it.toMutableList() }
        val selectedValue = newCells[row][col].value

        for (r in 0 until 9) {
            for (c in 0 until 9) {
                var isCurrentlyHighlighted = false
                if (r == row || c == col || ((r / 3 == row / 3) && (c / 3 == col / 3))) {
                    isCurrentlyHighlighted = true
                }
                if (selectedValue != null && newCells[r][c].value == selectedValue) {
                    isCurrentlyHighlighted = true
                }

                newCells[r][c] = newCells[r][c].copy(isHighlighted = isCurrentlyHighlighted)
            }
        }
        newCells[row][col] = newCells[row][col].copy(isHighlighted = true)

        return SudokuBoard(newCells.map { it.toList() })
    }

    fun clearHighlights(): SudokuBoard {
        val newCells = cells.toMutableList().map { it.toMutableList() }
        for (r in 0 until 9) {
            for (c in 0 until 9) {
                newCells[r][c] = newCells[r][c].copy(isHighlighted = false)
            }
        }
        return SudokuBoard(newCells.map { it.toList() })
    }
}