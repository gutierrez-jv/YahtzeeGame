package com.example.yahtzeegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class YahtzeeViewModel : ViewModel() {
    var rollCount by mutableStateOf(0)
        private set

    var heldDice by mutableStateOf(
        List(5) { false }
    )
        private set

    var isRolling by mutableStateOf(false)
        private set

    var categoryScores by mutableStateOf(
        emptyList<CategoryScore>()
    )
        private set
    var diceValues by mutableStateOf(
        listOf(1, 1, 1, 1, 1)
    )
        private set

    private fun rollDice(): Int {

        return Random.nextInt(1, 7)
    }

    fun toggleHold(index: Int) {
        if (rollCount !in 1..2 || isRolling) {
            return
        }

        heldDice = heldDice.mapIndexed { diceIndex, isHeld ->
            if (diceIndex == index) !isHeld else isHeld
        }
    }

    fun rollWithCoroutine() {
        if (rollCount >= 3 || isRolling) {
            return
        }

        viewModelScope.launch {
            isRolling = true

            repeat(10) {
                diceValues = diceValues.mapIndexed { index, value ->
                    if (heldDice[index]) {
                        value
                    } else {
                        rollDice()
                    }
                }

                delay(100.milliseconds)
            }

            rollCount += 1
            evaluateDice()
            isRolling = false
        }
    }

    fun startNewTurn() {
        rollCount = 0
        diceValues = List(5) { 1 }
        categoryScores = emptyList()
        heldDice = List(5) { false }
    }
    fun saveScore(category: YahtzeeCategory) {
        if (!canChooseCategory || category in savedScores) {
            return
        }

        val score = DiceRules.scoreFor(
            category = category,
            dice = diceValues
        )

        savedScores = savedScores + (category to score)

        if (isGameOver) {
            categoryScores = emptyList()
            heldDice = List(5) { false }
        } else {
            startNewTurn()
        }
    }

    fun startNewGame() {
        savedScores = emptyMap()
        startNewTurn()
    }
        var savedScores by mutableStateOf(
            emptyMap<YahtzeeCategory, Int>()
        )
            private set

        val totalScore: Int
            get() = savedScores.values.sum()

        val isGameOver: Boolean
            get() = savedScores.size == YahtzeeCategory.entries.size

        val canChooseCategory: Boolean
            get() = rollCount == 3 && !isRolling && !isGameOver

        val unusedCategoryScores: List<CategoryScore>
            get() = YahtzeeCategory.entries
                .filter { category -> category !in savedScores }
                .map { category ->
                    CategoryScore(
                        category = category,
                        score = DiceRules.scoreFor(
                            category = category,
                            dice = diceValues
                        )
                    )
                }

    private fun evaluateDice() {

        categoryScores =
            DiceRules
                .getAvailableCategories(diceValues)
                .map { category ->

                    CategoryScore(
                        category = category,
                        score = DiceRules.scoreFor(
                            category = category,
                            dice = diceValues
                        )
                    )
                }
    }
}