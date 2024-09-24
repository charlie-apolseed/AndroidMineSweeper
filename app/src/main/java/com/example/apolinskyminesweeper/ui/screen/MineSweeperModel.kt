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

    fun startGame() {
        // Initialize the board and solution board
        board = Array(5) { Array(5) { "" } }
        solutionBoard = Array(5) { Array(5) { "" } }
        var mineCount = 3
        while (mineCount > 0) {
            val x = Random.nextInt(5)
            val y = Random.nextInt(5)
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
                getAdjacentMines(row, col)
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

    fun getAdjacentMines(row: Int, col: Int) {
        var mineCount = 0
        for (i in (row - 1)..(row + 1)) {
            for (j in (col - 1)..(col + 1)) {
                if ((i in 0..4 && j in 0..4) && (solutionBoard[i][j] == "mine")) {
                    mineCount += 1
                }
            }
        }
        board[row][col] = mineCount.toString()
        Log.d("Board", board.contentDeepToString())
    }

    fun endGame() {
        Log.d("Game Over", "Game Over")
        isGameOver = true
    }
}