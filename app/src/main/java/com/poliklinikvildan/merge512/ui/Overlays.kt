package com.poliklinikvildan.merge512.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poliklinikvildan.merge512.R

@Composable
fun GameOverOverlay(onRestart: () -> Unit, modifier: Modifier = Modifier) {
    OverlayContainer(modifier = modifier) {
        Text(
            text = stringResource(R.string.game_over),
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Button(
            onClick = onRestart,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(stringResource(R.string.try_again), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun WinOverlay(onKeepPlaying: () -> Unit, onRestart: () -> Unit, modifier: Modifier = Modifier) {
    OverlayContainer(modifier = modifier) {
        Text(
            text = stringResource(R.string.you_win),
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Column(
            modifier = Modifier.padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onKeepPlaying,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(stringResource(R.string.keep_going), color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
            }
            TextButton(onClick = onRestart, modifier = Modifier.padding(top = 8.dp)) {
                Text(stringResource(R.string.new_game), color = Color.White)
            }
        }
    }
}

@Composable
private fun OverlayContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xCC1D1B26)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            content()
        }
    }
}
