package com.kanawish.sample.hello.util

/**
 * Simple utility to run a block of code at a given frequency.
 */
class Sampler(private val frequency:Int, var count:Int=0) {
    fun sample(block:Sampler.()->Unit) {
        if((count % frequency) == 0) block()
        count++
    }
}