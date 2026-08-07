package com.poliklinikvildan.merge512

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.poliklinikvildan.merge512.ui.GameScreen
import com.poliklinikvildan.merge512.ui.theme.Merge512Theme

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
