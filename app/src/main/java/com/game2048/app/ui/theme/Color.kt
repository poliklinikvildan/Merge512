package com.game2048.app.ui.theme

import androidx.compose.ui.graphics.Color

// Dark theme palette (Material 3 tonal, warm amber accent matching Merge 512 tiles).
val DarkBackground = Color(0xFF121117)
val DarkSurface = Color(0xFF1D1B26)
val DarkSurfaceVariant = Color(0xFF2A2734)
val DarkOnBackground = Color(0xFFEDEAF2)
val DarkOnSurface = Color(0xFFEDEAF2)
val DarkPrimary = Color(0xFFEDC22E)
val DarkOnPrimary = Color(0xFF1A1200)
val DarkSecondary = Color(0xFF8D8A99)
val DarkOnSecondary = Color(0xFF1A1200)

// Merge 512 tile colors keyed by value.
val TileEmpty = Color(0xFF2A2734)
val Tile2 = Color(0xFFEEE4DA)
val Tile4 = Color(0xFFEDE0C8)
val Tile8 = Color(0xFFF2B179)
val Tile16 = Color(0xFFF59563)
val Tile32 = Color(0xFFF67C5F)
val Tile64 = Color(0xFFF65E3B)
val Tile128 = Color(0xFFEDCF72)
val Tile256 = Color(0xFFEDCC61)
val Tile512 = Color(0xFFEDC850)
val Tile1024 = Color(0xFFEDC53F)
val Tile2048 = Color(0xFFEDC22E)
val TileSuper = Color(0xFF3C3A32)

val TextDark = Color(0xFF6B6560)
val TextLight = Color(0xFFFFFFFF)

fun tileColor(value: Int): Color = when (value) {
    0 -> TileEmpty
    2 -> Tile2
    4 -> Tile4
    8 -> Tile8
    16 -> Tile16
    32 -> Tile32
    64 -> Tile64
    128 -> Tile128
    256 -> Tile256
    512 -> Tile512
    1024 -> Tile1024
    2048 -> Tile2048
    else -> TileSuper
}

fun tileTextColor(value: Int): Color = when (value) {
    0 -> TextDark
    2 -> TextDark
    4 -> TextDark
    else -> TextLight
}
