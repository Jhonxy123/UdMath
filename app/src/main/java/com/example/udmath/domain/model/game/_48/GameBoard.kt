package com.example.udmath.domain.model.game._48

data class GameBoard(val board: Array<Array<Int>>) {

    fun move(direction: Direction): GameBoard {
        return when (direction) {
            Direction.UP -> moveUp()
            Direction.DOWN -> moveDown()
            Direction.LEFT -> moveLeft()
            Direction.RIGHT -> moveRight()
        }
    }

    private fun compressAndMergeLine(line: List<Int>): List<Int> {
        val nonZero = line.filter { it != 0 }.toMutableList()
        val result = mutableListOf<Int>()
        var i = 0
        while (i < nonZero.size) {
            if (i + 1 < nonZero.size && nonZero[i] == nonZero[i + 1]) {
                result.add(nonZero[i] * 2)
                i += 2
            } else {
                result.add(nonZero[i])
                i += 1
            }
        }
        while (result.size < 4) result.add(0)
        return result
    }

    fun moveLeft(): GameBoard {
        val newBoard = Array(4) { row ->
            compressAndMergeLine(board[row].toList()).toTypedArray()
        }
        return if (isSameBoard(newBoard)) this else GameBoard(newBoard)
    }

    fun moveRight(): GameBoard {
        val newBoard = Array(4) { row ->
            compressAndMergeLine(board[row].reversed().toList()).reversed().toTypedArray()
        }
        return if (isSameBoard(newBoard)) this else GameBoard(newBoard)
    }

    fun moveUp(): GameBoard {
        val newBoard = Array(4) { Array(4) { 0 } }
        for (col in 0 until 4) {
            val column = (0 until 4).map { board[it][col] }
            val merged = compressAndMergeLine(column)
            for (row in 0 until 4) {
                newBoard[row][col] = merged[row]
            }
        }
        return if (isSameBoard(newBoard)) this else GameBoard(newBoard)
    }

    fun moveDown(): GameBoard {
        val newBoard = Array(4) { Array(4) { 0 } }
        for (col in 0 until 4) {
            val column = (0 until 4).map { board[it][col] }.reversed()
            val merged = compressAndMergeLine(column).reversed()
            for (row in 0 until 4) {
                newBoard[row][col] = merged[row]
            }
        }
        return if (isSameBoard(newBoard)) this else GameBoard(newBoard)
    }

    fun addRandomTile(): GameBoard {
        val emptyCells = board.flatMapIndexed { row, rowList ->
            rowList.mapIndexedNotNull { col, value -> if (value == 0) row to col else null }
        }
        if (emptyCells.isEmpty()) return this
        val (row, col) = emptyCells.random()
        val value = if ((1..10).random() <= 9) 2 else 4
        val newBoard = board.map { it.copyOf() }.toTypedArray()
        newBoard[row][col] = value
        return GameBoard(newBoard)
    }

    fun hasAvailableMoves(): Boolean {
        if (board.any { row -> row.any { it == 0 } }) return true
        for (row in 0 until 4) {
            for (col in 0 until 4) {
                val current = board[row][col]
                if (col + 1 < 4 && board[row][col + 1] == current) return true
                if (row + 1 < 4 && board[row + 1][col] == current) return true
            }
        }
        return false
    }

    fun hasWon(): Boolean {
        return board.any { row -> row.any { it == 2048 } }
    }

    private fun isSameBoard(other: Array<Array<Int>>): Boolean {
        for (r in 0..3) {
            for (c in 0..3) {
                if (board[r][c] != other[r][c]) return false
            }
        }
        return true
    }
}