package com.kanawish.sample.hello

import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import kotlin.test.Test
import kotlin.test.assertEquals

class ExampleUnitTest {
    private val t = Terminal(AnsiLevel.TRUECOLOR)

    @Test
    fun addition_isCorrect() {
        t.println(brightBlue("addition_isCorrect"))
        assertEquals(4, 2 + 2)
    }
}
