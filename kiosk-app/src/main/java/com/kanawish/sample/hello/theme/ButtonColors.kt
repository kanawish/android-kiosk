package com.kanawish.sample.hello.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object ButtonColors {
    @Composable
    fun blueButtonColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            containerColor = Blue500,
            contentColor = Color.White
        )
    }

    @Composable
    fun secondaryButtonColors(): ButtonColors {
        return ButtonDefaults.buttonColors(
            contentColor = Color.White,
            containerColor = Color(0xFF131313)
        )
    }
}
