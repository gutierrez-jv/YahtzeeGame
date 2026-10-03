package com.example.yahtzeegame

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Text(
            text = "Yahtzee",
            style = MaterialTheme.typography.headlineSmall
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            viewModel.diceValues.forEachIndexed { index, value ->
                Die(
                    value = value,
                    isHeld = viewModel.heldDice[index],
                    canHold = viewModel.rollCount in 1..2 && !viewModel.isRolling,
                    onClick = {
                        viewModel.toggleHold(index)
                    }
                )
            }
        }
        Text(
            text = "Rolls: ${viewModel.rollCount} / 3"
        )
        Button(
            onClick = { viewModel.rollWithCoroutine() },
            enabled = viewModel.rollCount < 3 && !viewModel.isRolling
        ) {
            Text(
                if (viewModel.isRolling) "Rolling..."
                else "Roll"
            )
        }
        Button(
            onClick = { viewModel.startNewTurn() }
        ) {
            Text("New Turn")
        }
        viewModel.categoryScores.forEach { result ->

            Text(
                text = "${result.category}: ${result.score}"
            )
        }
        Text(
            text = "Total score: ${viewModel.totalScore}",
            style = MaterialTheme.typography.titleSmall
        )

        Text(
            text = "Scorecard",
            style = MaterialTheme.typography.titleMedium
        )

        viewModel.savedScores.forEach { (category, score) ->
            Text(
                text = "${category.name.replace("_", " ")}: $score"
            )
        }

        viewModel.unusedCategoryScores.forEach { result ->
            Button(
                onClick = {
                    viewModel.saveScore(result.category)
                },
                enabled = viewModel.canChooseCategory
            ) {
                Text(
                    text = "Save ${result.category.name.replace("_", " ")}: ${result.score}"
                )
            }
        }

        if (viewModel.isGameOver) {
            Text(
                text = "Game over! Final score: ${viewModel.totalScore}"
            )

            Button(
                onClick = {
                    viewModel.startNewGame()
                }
            ) {
                Text("Start New Game")
            }
        }
    }
}

@Composable
fun Die(
    value: Int,
    isHeld: Boolean,
    canHold: Boolean,
    onClick: () -> Unit
) {
    val diceImage = when (value) {
        1 -> R.drawable.die_1
        2 -> R.drawable.die_2
        3 -> R.drawable.die_3
        4 -> R.drawable.die_4
        5 -> R.drawable.die_5
        6 -> R.drawable.die_6
        else -> R.drawable.die_1
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = diceImage),
            contentDescription = "Dice showing $value",
            modifier = Modifier
                .size(64.dp)
                .clickable(
                    enabled = canHold,
                    onClick = onClick
                )
        )

        Text(
            text = if (isHeld) "HELD" else " "
        )
    }
}