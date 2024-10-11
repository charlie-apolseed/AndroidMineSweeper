package com.example.apolinskyminesweeper.ui.screen


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.random.Random



class MineSweeperModel : ViewModel() {
    var boardSize by mutableStateOf(5)
    var board by mutableStateOf(Array(boardSize) { Array(boardSize) { "" } })
    var solutionBoard by mutableStateOf(Array(boardSize) { Array(boardSize) { "" } })
    var flagMode by mutableStateOf(false)
    var isGameOver by mutableStateOf(false)
    var isGameWon by mutableStateOf(false)
    var mineCount by mutableStateOf(3)


    fun startGame() {

        board = Array(boardSize) { Array(boardSize) { "" } }
        solutionBoard = Array(boardSize) { Array(boardSize) { "" } }
        while (mineCount > 0) {
            val x = Random.nextInt(boardSize)
            val y = Random.nextInt(boardSize)
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

    fun updateBoardSize (size: Int) {
        boardSize = size
        mineCount = when (size) {
            5 -> 3
            7 -> 6
            10 -> 15
            else -> 3
        }
        startGame()
    }

    fun onCellClick(row: Int, col: Int) {
        if (!flagMode) {
            if (solutionBoard[row][col] == "") {
                board[row][col] = getAdjacentMines(row, col).toString()
            } else if (solutionBoard[row][col] == "mine" && board[row][col] == "") {
                endGame()
            }
        } else { //Flag mode
            if (solutionBoard[row][col] == "mine") {
                board[row][col] = "flag"
            } else if (solutionBoard[row][col] == "" && board[row][col] == "") {
                endGame()
            }
        }
        if (isBoardFull()) {
            isGameOver = true
            isGameWon = true
        }

        board = board.copyOf()
    }

    fun isBoardFull(): Boolean {
        for (row in board) {
            for (cell in row) {
                if (cell == "") {
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
                if ((i in 0..(boardSize - 1) && j in 0..(boardSize - 1)) && (solutionBoard[i][j] == "mine")) {
                    mineCount += 1
                }
            }
        }
        if (mineCount == 0) {
            val isRevealed = Array(boardSize) { Array(boardSize) { false } }
            revealEmptyArea(row, col, isRevealed)
        }
        Log.d("Board", board.contentDeepToString())
        return mineCount
    }

    fun revealEmptyArea(row: Int, col: Int, isRevealed: Array<Array<Boolean>> = Array(boardSize) { Array(boardSize) { false } }) {
        isRevealed[row][col] = true
        for (i in (row - 1)..(row + 1)) {
            for (j in (col - 1)..(col + 1)) {
                if ((i in 0..(boardSize - 1) && j in 0..(boardSize - 1)) && (solutionBoard[i][j] == "mine")) {
                    return
                }
            }
        }
        board[row][col] = "0"
        if (row + 1 < boardSize) {
            if (!isRevealed[row + 1][col]) {
                revealEmptyArea(row + 1, col, isRevealed)
            }
        }
        if (row - 1 >= 0) {
            if (!isRevealed[row - 1][col]) {
                revealEmptyArea(row - 1, col, isRevealed)
            }
        }
        if (col + 1 < boardSize) {
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