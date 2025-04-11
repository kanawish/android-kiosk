package com.kanawish.sample.hello

import android.app.Application
import android.graphics.Color
import android.os.Build
import com.google.android.things.contrib.driver.apa102.Apa102
import com.google.android.things.contrib.driver.ht16k33.Ht16k33
import com.google.android.things.contrib.driver.rainbowhat.RainbowHat
import com.kanawish.sample.hello.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class MainApplication : Application() {
    private val applicationScope:CoroutineScope by inject()

    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule())
        }

        // NOTE: Can't runtime android-things on unsupported devices.
        if( Build.DEVICE == "odroidn2") pimoHat()
    }

    fun pimoHat() {
        if( Build.DEVICE == "odroidn2") return

        val gpio = RainbowHat.openLedBlue()
        gpio.setValue(true)
        gpio.close()

        val ledstrip: Apa102 = RainbowHat.openLedStrip()
        ledstrip.setBrightness(1)
        val rainbow = IntArray(RainbowHat.LEDSTRIP_LENGTH)
        for (i in rainbow.indices) {
            rainbow[i] = Color.HSVToColor(255, floatArrayOf(i * 360f / rainbow.size, 1.0f, 1.0f))
        }
        ledstrip.write(rainbow)
        // Close the device when done.
        ledstrip.close()

        applicationScope.launch {
            var counter = 0
            while (true) {
                display("/${counter%4}/")
                counter++
                kotlinx.coroutines.delay(1000) // 1 second delay
            }
        }

        // Play a note on the buzzer.
/*
        val buzzer = RainbowHat.openPiezo()
        applicationScope.launch {
            buzzer.play(440.0)
            delay(500)
            // Stop the buzzer.
            buzzer.stop()
            // Close the device when done.
            buzzer.close()
        }
*/

    }

    private fun display(s: String) {
        // Display a string on the segment display.
        val segment = RainbowHat.openDisplay()
        segment.setBrightness(Ht16k33.HT16K33_BRIGHTNESS_MAX)
        segment.display(s)
        segment.setEnabled(true)
        // Close the device when done.
        segment.close()
    }
}

