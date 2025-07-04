package com.example.udmath.presentation.home.retos.game._48

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.udmath.domain.model.game._48.Direction
import com.example.udmath.domain.model.game._48.GameBoard


class GameViewModel : ViewModel() {
    private val _board = MutableLiveData(
        GameBoard(Array(4) { Array(4) { 0 } }.apply {
            this[0][0] = 2
            this[1][1] = 2
        })
    )
    val board: LiveData<GameBoard> = _board

    private val _gameOver = MutableLiveData(false)
    val gameOver: LiveData<Boolean> = _gameOver

    private val _hasWon = MutableLiveData(false)
    val hasWon: LiveData<Boolean> = _hasWon

    fun move(direction: Direction) {
        val currentBoard = _board.value ?: return
        val movedBoard = currentBoard.move(direction)

        // Imprimir en consola para ver si se ejecuta
        println("Intentando mover en dirección: $direction")

        if (movedBoard != currentBoard) {
            val newBoard = movedBoard.addRandomTile()
            _board.value = newBoard
            _hasWon.value = newBoard.hasWon()
            _gameOver.value = !newBoard.hasAvailableMoves()
            println("Movimiento aplicado y nueva ficha generada")
        } else {
            println("El tablero no cambió")
        }
    }
    fun resetGame() {
        _board.value = GameBoard(Array(4) { Array(4) { 0 } }).addRandomTile().addRandomTile()
        _gameOver.value = false
        _hasWon.value = false
    }

}