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
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Row(
            modifier = modifier.fillMaxWidth(.9f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = mineSweeperModel::startGame,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD357FE),
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = stringResource(R.string.start_game),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ))
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = stringResource(R.string.flag_mode), modifier = Modifier.padding(end = 8.dp) ,
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                Switch(
                    checked = mineSweeperModel.flagMode,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFD357FE), // Color of the thumb when checked
                        checkedTrackColor = Color(0xFFDDB8FA), // Color of the track when checked
                        uncheckedThumbColor = Color.Gray,       // Color of the thumb when unchecked
                        uncheckedTrackColor = Color.LightGray   // Color of the track when unchecked
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
                        val col = (offset.x / (size.width / 5)).toInt()
                        val row = (offset.y / (size.width / 5)).toInt()
                        Log.d("Click: ", "row: $row, col: $col")
                        mineSweeperModel.onCellClick(row, col)
                    }
                }
        ) {
            drawRect(
                Color(0xFF888888),
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

            mineSweeperModel.board.forEachIndexed { row, rowData ->
                rowData.forEachIndexed { col, value ->
                    if (value != "") {
                        drawRect( //change the color of the background square
                            Color.LightGray,
                            topLeft = Offset((size.width / 5) * col, (size.width / 5) * row),
                            size = Size(size.width / 5, size.width / 5)
                        )
                        if (value == "flag") {
                            drawImage(imageFlagIcon,
                                srcOffset = IntOffset(0,0),
                                srcSize = IntSize(imageFlagIcon.width,imageFlagIcon.height),
                                dstOffset = IntOffset(((size.width / 5) * (col) + (size.width / 35)).toInt(),
                                    ((size.width / 5) * (row) + (size.height / 45)).toInt()
                                ),
                                dstSize = IntSize((size.width / 6).toInt(),
                                    (size.width / 6).toInt()
                                )
                            )
//                            drawCircle(
//                                Color.Red,
//                                radius = size.width / 10,
//                                center = Offset(
//                                    (size.width / 5) * (col + .5f),
//                                    (size.width / 5) * (row + .5f)
//                                )
//                            )
                        } else {
                            // Measure the text before drawing
                            val textLayoutResult = textMeasurer.measure(
                                text = value,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontSize = (size.width / 14).sp,  // Adjust text size based on cell size
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            // Draw the text at the center of each cell
                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    (size.width / 5) * col + (size.width / 10) - textLayoutResult.size.width / 2,
                                    (size.width / 5) * row + (size.width / 10) - textLayoutResult.size.height / 2
                                )
                            )
                        }
                    }


                }
            }
        }
        // Game Over Popup
        if (mineSweeperModel.isGameOver) {
            AlertDialog(
                onDismissRequest = { /* Handle dismiss request */ },
                confirmButton = {
                    Button(
                        onClick = {
                            mineSweeperModel.startGame() // Restart the game
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD357FE), // Pink color
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

