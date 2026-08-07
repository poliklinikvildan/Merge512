package com.game2048.app.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.game2048.app.engine.GameEngine
import com.game2048.app.ui.theme.tileColor
import com.game2048.app.ui.theme.tileTextColor

/**
 * The 4x4 board with smooth appear-and-merge animations. Each cell is keyed
 * by its value so that Compose recomposes only the tiles that changed.
 */
@Composable
fun GameBoard(board: Array<IntArray>, modifier: Modifier = Modifier) {
    val padding = 8.dp
    val spacing = 8.dp
    val cornerRadius = 8.dp

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(cornerRadius))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(padding)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
            for (r in 0 until GameEngine.SIZE) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(spacing)
                ) {
                    for (c in 0 until GameEngine.SIZE) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                                .clip(RoundedCornerShape(cornerRadius))
                                .background(tileColor(0))
                        )
                    }
                }
            }
        }

        // Foreground tiles layer, overlaid on the empty grid.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(spacing)) {
                for (r in 0 until GameEngine.SIZE) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        for (c in 0 until GameEngine.SIZE) {
                            val value = board[r][c]
                            key(value, r, c) {
                                Tile(value = value, cornerRadius = cornerRadius)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RowScope.Tile(value: Int, cornerRadius: androidx.compose.ui.unit.Dp) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(value) {
        if (value != 0) {
            alpha.snapTo(1f)
            scale.snapTo(0f)
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 180, easing = LinearOutSlowInEasing)
            )
        } else {
            alpha.snapTo(0f)
            scale.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxSize()
            .scale(scale.value)
            .alpha(alpha.value)
            .clip(RoundedCornerShape(cornerRadius))
            .background(tileColor(value)),
        contentAlignment = Alignment.Center
    ) {
        if (value != 0) {
            val fontSize = when {
                value < 100 -> 36.sp
                value < 1000 -> 30.sp
                value < 10000 -> 26.sp
                else -> 22.sp
            }
            Text(
                text = value.toString(),
                color = tileTextColor(value),
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
