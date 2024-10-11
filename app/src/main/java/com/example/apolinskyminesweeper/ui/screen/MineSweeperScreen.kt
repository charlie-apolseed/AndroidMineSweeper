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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apolinskyminesweeper.R



@Composable
fun MineSweeperScreen(
    modifier: Modifier,
    viewModel: MineSweeperModel = viewModel()
) {
    val mineSweeperModel = viewModel
    val textMeasurer = rememberTextMeasurer()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = modifier
                .fillMaxWidth(.9f)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { mineSweeperModel.updateBoardSize(5) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE090E0),
                    contentColor = Color.Black
                )
            ) {
                Text(text = stringResource(R.string._5x5), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            }

            Button(
                onClick = { mineSweeperModel.updateBoardSize(7) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE044E0),
                    contentColor = Color.Black
                )
            ) {
                Text(text = stringResource(R.string._7x7), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            }

            Button(
                onClick = { mineSweeperModel.updateBoardSize(10) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFBA11BA),
                    contentColor = Color.Black
                )
            ) {
                Text(text = stringResource(R.string._10x10), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold))
            }
        }

        Row(
            modifier = modifier.fillMaxWidth(.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = mineSweeperModel::startGame,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFDB56A4),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(R.string.start_game),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.flag_mode),
                    modifier = Modifier.padding(end = 8.dp),
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Switch(
                    checked = mineSweeperModel.flagMode,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF7356db),
                        checkedTrackColor = Color(0xFFc0b7fb),
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.LightGray
                    ),
                    onCheckedChange = {
                        mineSweeperModel.toggleFlagMode()
                    }
                )
            }
        }

        val imageFlagIcon = ImageBitmap.imageResource(R.drawable.flagicon)

        Canvas(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .aspectRatio(1.0f)
                .pointerInput(key1 = Unit) {
                    detectTapGestures { offset -> // offset is the position of the tap
                        Log.d("Click: ", "x: ${offset.x}, y: ${offset.y}")
                        val col = (offset.x / (size.width / mineSweeperModel.boardSize)).toInt()
                        val row = (offset.y / (size.width / mineSweeperModel.boardSize)).toInt()
                        Log.d("Click: ", "row: $row, col: $col")
                        mineSweeperModel.onCellClick(row, col)
                    }
                }
        ) {
            drawRect(
                Color(0xFF888888),
                topLeft = Offset.Zero,
            )

            for (i in 1..(mineSweeperModel.boardSize-1)) { //Draw the grid lines
                drawLine(
                    Color.Black,
                    start = Offset(i * size.width /mineSweeperModel.boardSize , 0f),
                    end = Offset(i * size.width /mineSweeperModel.boardSize , size.height),
                    strokeWidth = 5f
                )
                drawLine(
                    Color.Black,
                    start = Offset(0f, i * size.width / mineSweeperModel.boardSize),
                    end = Offset(size.width, i * size.width / mineSweeperModel.boardSize),
                    strokeWidth = 5f
                )
            }

            mineSweeperModel.board.forEachIndexed { row, rowData ->
                rowData.forEachIndexed { col, value ->
                    val cellSize = size.width / mineSweeperModel.boardSize

                    if (value != "") {
                        drawRect(
                            Color.LightGray,
                            topLeft = Offset(cellSize * col, cellSize * row),
                            size = Size(cellSize, cellSize)
                        )

                        if (value == "flag") {

                            drawImage(
                                imageFlagIcon,
                                srcOffset = IntOffset(0, 0),
                                srcSize = IntSize(imageFlagIcon.width, imageFlagIcon.height),
                                dstOffset = IntOffset(
                                    (cellSize * col + (cellSize / 6)).toInt(),
                                    (cellSize * row + (cellSize / 6)).toInt()
                                ),
                                dstSize = IntSize((cellSize / 1.5).toInt(), (cellSize / 1.5).toInt())
                            )
                        } else {

                            val textSize = (cellSize / 2.5).sp
                            val textLayoutResult = textMeasurer.measure(
                                text = value,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = textSize,
                                    fontWeight = FontWeight.Bold
                                )
                            )

                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    cellSize * col + cellSize / 2 - textLayoutResult.size.width / 2,
                                    cellSize * row + cellSize / 2 - textLayoutResult.size.height / 2
                                )
                            )
                        }
                    }
                }
            }

        }

        if (mineSweeperModel.isGameOver) {
            AlertDialog(
                onDismissRequest = { /* Handle dismiss request */ },
                confirmButton = {
                    Button(
                        onClick = {
                            mineSweeperModel.startGame()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFDB56A4),
                            contentColor = Color.Black
                        )
                    ) {
                        Text(stringResource(R.string.restart))
                    }
                },
                title = { Text(stringResource(R.string.game_over)) },
                text = {
                    Text(
                        if (mineSweeperModel.isGameWon) stringResource(R.string.you_won) else stringResource(
                            R.string.you_lost
                        ),
                        style = TextStyle(fontSize = 18.sp) // Optional: Adjust text size
                    )
                }
            )
        }
    }
}
