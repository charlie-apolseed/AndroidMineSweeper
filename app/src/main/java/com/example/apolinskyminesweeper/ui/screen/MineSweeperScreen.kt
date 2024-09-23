package com.example.apolinskyminesweeper.ui.screen


import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun MineSweeperScreen(modifier: Modifier,
                      viewModel: MineSweeperModel = viewModel()
)
{
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var MineSweeperModel = viewModel
        Text("Mine Sweeper")

        Row(modifier = modifier.fillMaxWidth(.9f), horizontalArrangement = Arrangement.SpaceBetween){
            Button(onClick = MineSweeperModel::startGame) {
                Text("Start Game")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Flag Mode", modifier = Modifier.padding(end = 8.dp))
                Switch(
                    checked = MineSweeperModel.flagMode,
                    onCheckedChange = {
                        MineSweeperModel.toggleFlagMode()
                    }
                )
            }
        }



        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1.0f)
                .pointerInput(key1 = Unit) {
                    detectTapGestures { offset -> // offset is the position of the tap
                        Log.d("Click: ", "x: ${offset.x}, y: ${offset.y}")
                        var col = (offset.x / (size.width / 5)).toInt()
                        var row = (offset.y / (size.width / 5)).toInt()
                        Log.d("Click: ", "row: $row, col: $col")
                        MineSweeperModel.onCellClick(row, col)
                    }
                }
        ) {
            drawRect(
                Color.LightGray,
                topLeft = Offset.Zero,
            )

            for (i in 1..4) { //Draw the grid lines
                drawLine(
                    Color.Black,
                    start = Offset(i * size.width / 5, 0f),
                    end = Offset(i * size.width / 5, size.height),
                    strokeWidth = 5f
                )
                drawLine(
                    Color.Black,
                    start = Offset(0f, i * size.width / 5),
                    end = Offset(size.width, i * size.width / 5),
                    strokeWidth = 5f
                )
            }

            MineSweeperModel.board.forEachIndexed { row, rowData ->
                rowData.forEachIndexed {
                    col, it ->
                    if (it == "flag") {
                        drawCircle(
                            Color.Red,
                            radius = size.width / 10,
                            center = Offset((size.width / 5) * (col + .5f), (size.width / 5) * (row + .5f) )
                        )
                    } else if (it != "") {
                        drawRect(
                            color = Color(0xFF888888),
                            topLeft = Offset((size.width / 5) * col, (size.width / 5) * row),
                            size = Size(size.width / 5, size.width / 5)
                        )
                    }
                }
            }
        }
    }
}

