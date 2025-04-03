package com.kanawish.sample.hello.roughs

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Composable effect that applies and maintains immersive mode.
 *
 * This is just a janky example pulled from Claude, it doesn't hold state
 * all that great when navigating between screens, etc.
 *
 * The general idea is here, for when someone wants to go full screen.
 */
@Composable
fun ImmersiveModeEffect() {
    val context = LocalContext.current
    DisposableEffect(true) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose { }

        val window = activity.window
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val insetsController = WindowInsetsControllerCompat(window, window.decorView)

        // Hide both the status bar and the navigation bar
        insetsController.hide(WindowInsetsCompat.Type.systemBars())
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // Set up lifecycle observer to maintain immersive mode
        val lifecycleObserver = LifecycleEventObserver { _: LifecycleOwner, event: Lifecycle.Event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                insetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
        }

        val lifecycle = (context as ComponentActivity).lifecycle
        lifecycle.addObserver(lifecycleObserver)

        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }
}