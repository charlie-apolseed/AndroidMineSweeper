package com.example.apolinskyminesweeper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.example.apolinskyminesweeper.ui.screen.MineSweeperScreen
import com.example.apolinskyminesweeper.ui.theme.ApolinskyMineSweeperTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApolinskyMineSweeperTheme {
                val logo = ImageBitmap.imageResource(R.drawable.logo)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(), // Set the background color to white
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 16.dp),  // Reduced the vertical padding
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            bitmap = logo,
                            contentDescription = "Logo",
                            modifier = Modifier
                                .padding(bottom = 8.dp)  // Reduced space between the logo and screen content
                                .padding(top = 20.dp)
                        )

                        // Removed the extra padding in MineSweeperScreen
                        MineSweeperScreen(modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

}