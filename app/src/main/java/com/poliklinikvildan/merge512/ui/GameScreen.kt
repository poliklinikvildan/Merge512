package com.poliklinikvildan.merge512.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.poliklinikvildan.merge512.R
import com.poliklinikvildan.merge512.engine.Direction
import com.poliklinikvildan.merge512.engine.GameViewModel
import kotlin.math.abs

@Composable
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val density = LocalDensity.current
    val swipeThresholdPx = with(density) { 40.dp.toPx() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        HeaderRow(
            score = state.score,
            bestScore = state.bestScore,
            onNewGame = viewModel::restart
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            val totalX = remember { mutableFloatStateOf(0f) }
            val totalY = remember { mutableFloatStateOf(0f) }
            var handled = false

            GameBoard(
                board = state.board,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                totalX.floatValue = 0f
                                totalY.floatValue = 0f
                                handled = false
                            },
                            onDragEnd = {
                                totalX.floatValue = 0f
                                totalY.floatValue = 0f
                                handled = false
                            },
                            onDragCancel = {
                                totalX.floatValue = 0f
                                totalY.floatValue = 0f
                                handled = false
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            if (handled) return@detectDragGestures
                            totalX.floatValue += dragAmount.x
                            totalY.floatValue += dragAmount.y
                            val dx = abs(totalX.floatValue)
                            val dy = abs(totalY.floatValue)
                            if (dx > swipeThresholdPx || dy > swipeThresholdPx) {
                                if (dx > dy) {
                                    viewModel.move(if (totalX.floatValue > 0) Direction.RIGHT else Direction.LEFT)
                                } else {
                                    viewModel.move(if (totalY.floatValue > 0) Direction.DOWN else Direction.UP)
                                }
                                handled = true
                            }
                        }
                    }
            )

            if (state.isGameOver) {
                GameOverOverlay(
                    onRestart = viewModel::restart,
                    modifier = Modifier.fillMaxSize()
                )
            } else if (state.isWon && !state.keepPlaying) {
                WinOverlay(
                    onKeepPlaying = viewModel::keepPlaying,
                    onRestart = viewModel::restart,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Text(
            text = stringResource(R.string.swipe_instructions),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun HeaderRow(score: Int, bestScore: Int, onNewGame: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
        Column(horizontalAlignment = Alignment.End) {
            ScoreBar(score = score, bestScore = bestScore)
            Button(
                onClick = onNewGame,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(
                    stringResource(R.string.new_game),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
