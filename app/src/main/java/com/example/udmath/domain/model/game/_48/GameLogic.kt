package com.example.udmath.domain.model.game._48

import kotlin.random.Random

class GameLogic {

    private var currentBoard: GameBoard
    private val boardSize = 4

    init {
        currentBoard = GameBoard(Array(boardSize) { Array(boardSize) { 0 } })
        addRandomTile()
        addRandomTile()
    }

    fun getBoard(): GameBoard = currentBoard

    fun move(direction: Direction): Boolean {
        val originalBoard = currentBoard.copy() // Guarda una copia para detectar si hubo movimiento
        var changed = false // Indica si la función slideAndMergeXxx realizó algún cambio

        when (direction) {
            Direction.UP -> changed = slideAndMergeUp()
            Direction.DOWN -> changed = slideAndMergeDown()
            Direction.LEFT -> changed = slideAndMergeLeft()
            Direction.RIGHT -> changed = slideAndMergeRight()
        }

        // Solo añade una ficha si el tablero realmente cambió
        // El operador != en GameBoard usa el override de equals para comparación profunda
        if (changed && currentBoard != originalBoard) {
            addRandomTile()
            return true
        }
        return false // No hubo movimiento que cambiara el tablero o no hubo fusiones
    }

    private fun slideAndMergeUp(): Boolean {
        var changed = false
        val newBoard = currentBoard.copy().board // Trabaja en una copia mutable del tablero

        for (col in 0 until boardSize) { // Itera por cada columna
            val columnValues = mutableListOf<Int>()
            // 1. Extraer solo los valores no cero en orden de arriba a abajo
            for (row in 0 until boardSize) {
                if (newBoard[row][col] != 0) {
                    columnValues.add(newBoard[row][col])
                }
            }

            // 2. Realizar fusiones en una nueva lista (de arriba a abajo)
            val mergedValues = mutableListOf<Int>()
            var i = 0
            while (i < columnValues.size) {
                if (i + 1 < columnValues.size && columnValues[i] == columnValues[i + 1]) {
                    mergedValues.add(columnValues[i] * 2) // Añadir valor fusionado
                    i += 2 // Saltar el siguiente elemento ya fusionado
                    changed = true
                } else {
                    mergedValues.add(columnValues[i]) // Añadir valor sin fusionar
                    i += 1
                }
            }

            // 3. Rellenar la columna en el nuevo tablero con ceros al final
            for (row in 0 until boardSize) { // Iterar de arriba a abajo para rellenar
                val valueToPlace = if (row < mergedValues.size) mergedValues[row] else 0
                if (newBoard[row][col] != valueToPlace) {
                    changed = true // Se marcó como cambiado si el valor es diferente
                }
                newBoard[row][col] = valueToPlace
            }
        }
        currentBoard = GameBoard(newBoard) // Actualiza el tablero actual
        return changed
    }

    private fun slideAndMergeDown(): Boolean {
        var changed = false
        val newBoard = currentBoard.copy().board

        for (col in 0 until boardSize) { // Itera por cada columna
            val columnValues = mutableListOf<Int>()
            // 1. Extraer solo los valores no cero en orden de abajo a arriba
            for (row in boardSize - 1 downTo 0) {
                if (newBoard[row][col] != 0) {
                    columnValues.add(newBoard[row][col])
                }
            }

            // 2. Realizar fusiones en una nueva lista (de abajo a arriba)
            val mergedValues = mutableListOf<Int>()
            var i = 0
            while (i < columnValues.size) {
                if (i + 1 < columnValues.size && columnValues[i] == columnValues[i + 1]) {
                    mergedValues.add(columnValues[i] * 2)
                    i += 2 // Saltar el siguiente elemento ya fusionado
                    changed = true
                } else {
                    mergedValues.add(columnValues[i])
                    i += 1
                }
            }

            // 3. Rellenar la columna en el nuevo tablero, con ceros al principio
            // Debemos rellenar el tablero de abajo a arriba, usando los valores fusionados.
            // Los elementos en mergedValues están en orden de abajo a arriba.
            // mergedValues[0] -> va a la posición más baja (boardSize - 1)
            // mergedValues[1] -> va a la siguiente posición más baja (boardSize - 2)
            for (row in boardSize - 1 downTo 0) { // Iterar de abajo a arriba para rellenar
                val valueToPlace: Int = if ((boardSize - 1) - row < mergedValues.size) {
                    // Si el índice calculado es válido para mergedValues
                    mergedValues[(boardSize - 1) - row]
                } else {
                    0 // Si no hay más valores fusionados, rellenar con cero
                }

                if (newBoard[row][col] != valueToPlace) {
                    changed = true
                }
                newBoard[row][col] = valueToPlace
            }
        }
        currentBoard = GameBoard(newBoard)
        return changed
    }

    private fun slideAndMergeLeft(): Boolean {
        var changed = false
        val newBoard = currentBoard.copy().board

        for (row in 0 until boardSize) { // Itera por cada fila
            val rowValues = mutableListOf<Int>()
            // 1. Extraer solo los valores no cero en orden de izquierda a derecha
            for (col in 0 until boardSize) {
                if (newBoard[row][col] != 0) {
                    rowValues.add(newBoard[row][col])
                }
            }

            // 2. Realizar fusiones en una nueva lista (de izquierda a derecha)
            val mergedValues = mutableListOf<Int>()
            var i = 0
            while (i < rowValues.size) {
                if (i + 1 < rowValues.size && rowValues[i] == rowValues[i + 1]) {
                    mergedValues.add(rowValues[i] * 2)
                    i += 2
                    changed = true
                } else {
                    mergedValues.add(rowValues[i])
                    i += 1
                }
            }

            // 3. Rellenar la fila en el nuevo tablero con ceros al final
            for (col in 0 until boardSize) { // Iterar de izquierda a derecha para rellenar
                val valueToPlace = if (col < mergedValues.size) mergedValues[col] else 0
                if (newBoard[row][col] != valueToPlace) {
                    changed = true
                }
                newBoard[row][col] = valueToPlace
            }
        }
        currentBoard = GameBoard(newBoard)
        return changed
    }

    private fun slideAndMergeRight(): Boolean {
        var changed = false
        val newBoard = currentBoard.copy().board

        for (row in 0 until boardSize) { // Itera por cada fila
            val rowValues = mutableListOf<Int>()
            // 1. Extraer solo los valores no cero en orden de derecha a izquierda
            for (col in boardSize - 1 downTo 0) {
                if (newBoard[row][col] != 0) {
                    rowValues.add(newBoard[row][col])
                }
            }

            // 2. Realizar fusiones en una nueva lista (de derecha a izquierda)
            val mergedValues = mutableListOf<Int>()
            var i = 0
            while (i < rowValues.size) {
                if (i + 1 < rowValues.size && rowValues[i] == rowValues[i + 1]) {
                    mergedValues.add(rowValues[i] * 2)
                    i += 2
                    changed = true
                } else {
                    mergedValues.add(rowValues[i])
                    i += 1
                }
            }

            // 3. Rellenar la fila en el nuevo tablero, con ceros al principio
            // Debemos rellenar el tablero de derecha a izquierda, usando los valores fusionados.
            // Los elementos en mergedValues están en orden de derecha a izquierda.
            // mergedValues[0] -> va a la posición más a la derecha (boardSize - 1)
            // mergedValues[1] -> va a la siguiente posición más a la derecha (boardSize - 2)
            for (col in boardSize - 1 downTo 0) { // Iterar de derecha a izquierda para rellenar
                val valueToPlace: Int = if ((boardSize - 1) - col < mergedValues.size) {
                    // Si el índice calculado es válido para mergedValues
                    mergedValues[(boardSize - 1) - col]
                } else {
                    0 // Si no hay más valores fusionados, rellenar con cero
                }

                if (newBoard[row][col] != valueToPlace) {
                    changed = true
                }
                newBoard[row][col] = valueToPlace
            }
        }
        currentBoard = GameBoard(newBoard)
        return changed
    }

    private fun addRandomTile() {
        val emptyCells = mutableListOf<Pair<Int, Int>>()
        for (row in 0 until boardSize) {
            for (col in 0 until boardSize) {
                if (currentBoard.board[row][col] == 0) {
                    emptyCells.add(Pair(row, col))
                }
            }
        }

        if (emptyCells.isNotEmpty()) {
            val randomCell = emptyCells.random()
            val newValue = if (Random.nextFloat() < 0.9) 2 else 4
            currentBoard.board[randomCell.first][randomCell.second] = newValue
        }
    }

    fun isGameOver(): Boolean {
        // Si el tablero está lleno
        if (currentBoard.board.flatten().none { it == 0 }) {
            // Se debe verificar si hay *algún* movimiento posible (incluyendo fusiones)
            // Una forma robusta es simular cada movimiento y ver si el tablero cambia.
            // Si el tablero está lleno Y ningún movimiento cambia el tablero, entonces es Game Over.

            val tempLogicForCheck = GameLogic()
            tempLogicForCheck.currentBoard = currentBoard.copy() // Usa una copia del tablero actual

            // Si al simular CUALQUIER movimiento el tablero cambia, el juego NO ha terminado.
            if (tempLogicForCheck.slideAndMergeUp() ||
                tempLogicForCheck.slideAndMergeDown() ||
                tempLogicForCheck.slideAndMergeLeft() ||
                tempLogicForCheck.slideAndMergeRight()) {
                return false // Todavía hay movimientos posibles
            }
            return true // No hay movimientos posibles y el tablero está lleno
        }
        return false // Todavía hay celdas vacías, por lo tanto no Game Over
    }

    fun hasWon(): Boolean {
        currentBoard.board.forEach { row ->
            row.forEach { value ->
                if (value == 2048) return true
            }
        }
        return false
    }
}