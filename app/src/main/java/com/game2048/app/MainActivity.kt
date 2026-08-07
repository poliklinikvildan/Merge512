package com.game2048.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.game2048.app.ui.GameScreen
import com.game2048.app.ui.theme.Merge512Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Merge512Theme {
                GameScreen()
            }
        }
    }
}
