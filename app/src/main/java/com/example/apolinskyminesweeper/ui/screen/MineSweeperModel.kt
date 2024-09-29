package com.example.apolinskyminesweeper.ui.screen


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random



class MineSweeperModel : ViewModel() {
    var board by mutableStateOf(Array(5) { Array(5) { "" } })
    var solutionBoard by mutableStateOf(Array(5) { Array(5) { "" } })
    var flagMode by mutableStateOf(false)
    var isGameOver by mutableStateOf(false)
    var isGameWon by mutableStateOf(false)
    final val BOARD_SIZE = 5

    fun startGame() {
        // Initialize the board and solution board
        board = Array(BOARD_SIZE) { Array(5) { "" } }
        solutionBoard = Array(BOARD_SIZE) { Array(BOARD_SIZE) { "" } }
        var mineCount = 3
        while (mineCount > 0) {
            val x = Random.nextInt(BOARD_SIZE)
            val y = Random.nextInt(BOARD_SIZE)
            if (solutionBoard[x][y] == "") {
                solutionBoard[x][y] = "mine"
                mineCount--
            }
        }
        flagMode = false
        isGameOver = false
        isGameWon = false
        Log.d("Solution Board", solutionBoard.contentDeepToString())
    }

    fun onCellClick(row: Int, col: Int) {
        if (!flagMode) {
            if (solutionBoard[row][col] == "") {
                board[row][col] = getAdjacentMines(row, col).toString()
            } else if (solutionBoard[row][col] == "mine") {
                endGame()
            }
        } else { //Flag mode
            if (solutionBoard[row][col] == "mine") {
                board[row][col] = "flag"
            } else if (solutionBoard[row][col] == "") {
                endGame()
            }
        }
        if (isBoardFull()) {
            isGameOver = true
            isGameWon = true
        }
        // Force board update
        board = board.copyOf() // This line triggers the recomposition
    }

    fun isBoardFull(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == "") { // Assuming empty cells are represented by an empty string
                    return false
                }
            }
        }
        return true
    }

    fun toggleFlagMode() {
        flagMode = !flagMode
    }

    fun getAdjacentMines(row: Int, col: Int): Int{
        var mineCount = 0
        for (i in (row - 1)..(row + 1)) {
            for (j in (col - 1)..(col + 1)) {
                if ((i in 0..(BOARD_SIZE - 1) && j in 0..(BOARD_SIZE - 1)) && (solutionBoard[i][j] == "mine")) {
                    mineCount += 1
                }
            }
        }
        if (mineCount == 0) {
            val isRevealed = Array(BOARD_SIZE) { Array(BOARD_SIZE) { false } }
            revealEmptyArea(row, col, isRevealed)
        }
        Log.d("Board", board.contentDeepToString())
        return mineCount
    }

    fun revealEmptyArea(row: Int, col: Int, isRevealed: Array<Array<Boolean>> = Array(BOARD_SIZE) { Array(BOARD_SIZE) { false } }) {
        isRevealed[row][col] = true
        for (i in (row - 1)..(row + 1)) {
            for (j in (col - 1)..(col + 1)) {
                if ((i in 0..(BOARD_SIZE - 1) && j in 0..(BOARD_SIZE - 1)) && (solutionBoard[i][j] == "mine")) {
                    return
                }
            }
        }
        board[row][col] = "0"
        if (row + 1 < BOARD_SIZE) {
            if (!isRevealed[row + 1][col]) {
                revealEmptyArea(row + 1, col, isRevealed)
            }
        }
        if (row - 1 >= 0) {
            if (!isRevealed[row - 1][col]) {
                revealEmptyArea(row - 1, col, isRevealed)
            }
        }
        if (col + 1 < BOARD_SIZE) {
            if (!isRevealed[row][col + 1]) {
                revealEmptyArea(row, col + 1, isRevealed)
            }
        }
        if (col - 1 >= 0) {
            if (!isRevealed[row][col - 1]) {
                revealEmptyArea(row, col - 1, isRevealed)
            }
        }
    }

    fun endGame() {
        Log.d("Game Over", "Game Over")
        isGameOver = true
    }
}