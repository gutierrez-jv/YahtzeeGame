package com.example.yahtzeegame.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.yahtzeegame.R
import com.example.yahtzeegame.YahtzeeViewModel


@Composable
fun YahtzeeScreen(
    viewModel: YahtzeeViewModel
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {

        Text(
            text = "Yahtzee"
        )

        Row {

            viewModel.diceValues.forEach { value ->

                Die(
                    value = value
                )
            }
        }
        Button( onClick = { viewModel.rollWithCoroutine()} ){
            Text("Roll")
        }
    }
}

@Composable
fun Die(value: Int) {

    val diceImage = when (value) {

        1 -> R.drawable.die_1
        2 -> R.drawable.die_2
        3 -> R.drawable.die_3
        4 -> R.drawable.die_4
        5 -> R.drawable.die_5
        6 -> R.drawable.die_6

        else -> R.drawable.die_1
    }

    Image(
        painter = painterResource(
            id = diceImage
        ),
        contentDescription = "Dice showing $value",
        modifier = Modifier.size(64.dp)
    )
}