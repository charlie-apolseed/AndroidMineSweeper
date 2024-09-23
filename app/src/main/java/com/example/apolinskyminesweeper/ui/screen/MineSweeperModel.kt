package com.example.apolinskyminesweeper.ui.screen

import android.app.GameState
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
        Log.d("Solution Board", solutionBoard.contentDeepToString())
    }

    fun onCellClick(row: Int, col: Int): Unit {
        if (!flagMode) { //Reveal mode
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
    }

    fun toggleFlagMode() {
        flagMode = !flagMode
    }

    fun getAdjacentMines(row: Int, col: Int): Unit {
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

    fun endGame(): Unit {
        Log.d("Game Over", "Game Over")
        //TODO Implement end game logic
    }
}