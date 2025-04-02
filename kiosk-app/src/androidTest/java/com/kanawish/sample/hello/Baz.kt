package com.kanawish.sample.hello

import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
import okio.FileSystem
import okio.Path.Companion.toPath
import org.junit.Assert.assertEquals
import org.junit.Test

class Baz {
    private val t = Terminal(AnsiLevel.TRUECOLOR)

    @Test
    fun addition_isCorrect() {
        t.println(brightBlue("addition_isCorrect"))
        assertEquals(4, 2 + 2)
    }

    fun okioNotes() {
        t.println(brightBlue("load file example"))
        val list = FileSystem.SYSTEM.list(".".toPath())
        list.forEach { println(it.name) }
        val file = FileSystem.SYSTEM.read("google-services.json".toPath()) {
            while (true) {
                val line = readUtf8Line() ?: break
                println(line)
            }
        }

        val blob = FileSystem.SYSTEM.read("google-services.json".toPath()) { readUtf8() }
        // TODO: Prettify
        println(blob)
    }
}