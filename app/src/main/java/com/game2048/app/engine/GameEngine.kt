package com.game2048.app.engine

/**
 * Pure game logic for Merge 512 on a 4x4 grid.
 *
 * The board is stored as a 4x4 array of Int where 0 means an empty cell.
 * All movement and merge logic is direction-agnostic: every move is reduced
 * to the [moveLeft] case by rotating the board, moving left, and rotating back.
 */
class GameEngine {

    companion object {
        const val SIZE = 4
        const val WIN_VALUE = 512
    }

    /** Returns a fresh board with two random starting tiles. */
    fun newBoard(): Array<IntArray> {
        val board = Array(SIZE) { IntArray(SIZE) }
        addRandomTile(board)
        addRandomTile(board)
        return board
    }

    /**
     * Places a new tile (2 with 90% probability, 4 with 10%) into a random
     * empty cell. Returns true if a tile was placed, false if the board is full.
     */
    fun addRandomTile(board: Array<IntArray>): Boolean {
        val empty = mutableListOf<IntArray>()
        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                if (board[r][c] == 0) empty.add(intArrayOf(r, c))
            }
        }
        if (empty.isEmpty()) return false
        val (r, c) = empty.random()
        board[r][c] = if ((0..9).random() < 9) 2 else 4
        return true
    }

    /**
     * Applies a move in [direction]. Mutates [board] in place and returns a
     * [MoveResult] describing whether the board changed, the score gained,
     * and whether any merge produced the 512 tile.
     */
    fun move(board: Array<IntArray>, direction: Direction): MoveResult {
        val before = copyBoard(board)

        // Reduce every direction to left by rotating.
        val rotations = when (direction) {
            Direction.LEFT -> 0
            Direction.UP -> 1
            Direction.RIGHT -> 2
            Direction.DOWN -> 3
        }
        repeat(rotations) { rotateClockwise(board) }

        var gained = 0
        var reached512 = false
        for (r in 0 until SIZE) {
            moveAndMergeRow(board, r, reached512Ref = { reached512 = true }, scoreRef = { gained += it })
        }

        repeat((4 - rotations) % 4) { rotateClockwise(board) }

        val changed = !boardsEqual(before, board)
        return MoveResult(changed = changed, scoreGained = gained, reached512 = reached512)
    }

    /** Returns true when no legal move remains (game over). */
    fun isGameOver(board: Array<IntArray>): Boolean {
        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                if (board[r][c] == 0) return false
                if (c + 1 < SIZE && board[r][c] == board[r][c + 1]) return false
                if (r + 1 < SIZE && board[r][c] == board[r + 1][c]) return false
            }
        }
        return true
    }

    /** Returns true when the 512 tile is present on the board. */
    fun hasWon(board: Array<IntArray>): Boolean {
        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                if (board[r][c] >= WIN_VALUE) return true
            }
        }
        return false
    }

    fun copyBoard(board: Array<IntArray>): Array<IntArray> =
        Array(SIZE) { r -> board[r].copyOf() }

    private fun boardsEqual(a: Array<IntArray>, b: Array<IntArray>): Boolean {
        for (r in 0 until SIZE) {
            for (c in 0 until SIZE) {
                if (a[r][c] != b[r][c]) return false
            }
        }
        return true
    }

    private fun moveAndMergeRow(
        board: Array<IntArray>,
        r: Int,
        reached512Ref: () -> Unit,
        scoreRef: (Int) -> Unit
    ) {
        val row = board[r]
        val compacted = IntArray(SIZE)
        var pos = 0
        for (c in 0 until SIZE) {
            if (row[c] != 0) compacted[pos++] = row[c]
        }
        // Merge adjacent equal tiles (no double merge: skip next after a merge).
        val merged = IntArray(SIZE)
        var writePos = 0
        var i = 0
        while (i < SIZE) {
            if (compacted[i] == 0) {
                i++
                continue
            }
            if (i + 1 < SIZE && compacted[i] == compacted[i + 1]) {
                val sum = compacted[i] * 2
                merged[writePos++] = sum
                scoreRef(sum)
                if (sum >= WIN_VALUE) reached512Ref()
                i += 2
            } else {
                merged[writePos++] = compacted[i]
                i++
            }
        }
        for (c in 0 until SIZE) row[c] = merged[c]
    }

    /** Rotates the board 90 degrees clockwise in place. */
    private fun rotateClockwise(board: Array<IntArray>) {
        for (r in 0 until SIZE / 2) {
            for (c in r until SIZE - 1 - r) {
                val temp = board[r][c]
                board[r][c] = board[SIZE - 1 - c][r]
                board[SIZE - 1 - c][r] = board[SIZE - 1 - r][SIZE - 1 - c]
                board[SIZE - 1 - r][SIZE - 1 - c] = board[c][SIZE - 1 - r]
                board[c][SIZE - 1 - r] = temp
            }
        }
    }
}

enum class Direction { LEFT, RIGHT, UP, DOWN }

data class MoveResult(
    val changed: Boolean,
    val scoreGained: Int,
    val reached512: Boolean
)
