package com.poliklinikvildan.merge512.engine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {

    private val engine = GameEngine()

    private val _state = MutableStateFlow(GameState())
    val state: StateFlow<GameState> = _state.asStateFlow()

    init {
        restart()
    }

    fun restart() {
        _state.update {
            it.copy(
                board = engine.newBoard(),
                score = 0,
                isGameOver = false,
                isWon = false,
                keepPlaying = false,
                lastMove = null
            )
        }
    }

    fun move(direction: Direction) {
        val current = _state.value
        if (current.isGameOver) return
        if (current.isWon && !current.keepPlaying) return

        viewModelScope.launch {
            val board = engine.copyBoard(current.board)
            val result = engine.move(board, direction)
            if (!result.changed) return@launch

            engine.addRandomTile(board)

            val newScore = current.score + result.scoreGained
            val won = result.reached512 || current.isWon
            val over = engine.isGameOver(board)

            _state.update {
                it.copy(
                    board = board,
                    score = newScore,
                    bestScore = maxOf(it.bestScore, newScore),
                    isGameOver = over,
                    isWon = won,
                    lastMove = direction
                )
            }
        }
    }

    fun keepPlaying() {
        _state.update { it.copy(keepPlaying = true) }
    }
}

data class GameState(
    val board: Array<IntArray> = Array(GameEngine.SIZE) { IntArray(GameEngine.SIZE) },
    val score: Int = 0,
    val bestScore: Int = 0,
    val isGameOver: Boolean = false,
    val isWon: Boolean = false,
    val keepPlaying: Boolean = false,
    val lastMove: Direction? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameState) return false
        if (score != other.score) return false
        if (bestScore != other.bestScore) return false
        if (isGameOver != other.isGameOver) return false
        if (isWon != other.isWon) return false
        if (keepPlaying != other.keepPlaying) return false
        if (lastMove != other.lastMove) return false
        for (r in board.indices) {
            if (!board[r].contentEquals(other.board[r])) return false
        }
        return true
    }

    override fun hashCode(): Int {
        var result = score
        result = 31 * result + bestScore
        result = 31 * result + isGameOver.hashCode()
        result = 31 * result + isWon.hashCode()
        result = 31 * result + keepPlaying.hashCode()
        result = 31 * result + (lastMove?.hashCode() ?: 0)
        result = 31 * result + board.contentDeepHashCode()
        return result
    }
}
