package com.kanawish.sample.hello.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kanawish.sample.hello.navigation.TypeRoute.*
import com.kanawish.sample.hello.view.MainScreen
import com.kanawish.sample.hello.view.CameraView
import org.koin.compose.koinInject

// TODO: Replace MainNavGraph with MainNavGraphHost
@Composable
fun MainNavGraphHost(mainNav: MainNav = koinInject()) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = MainRoute
    ) {
        composable<MainRoute> { MainScreen() }
        composable<QRScanRoute> { CameraView() }
    }

    LaunchedEffect(Unit) {
        timber.log.Timber.d("MainActivity -> LaunchedEffect(Unit)")
        mainNav.mainNavEvents.collect { event ->
            timber.log.Timber.d("mainNavEvent $event")
            event.block.invoke(navHostController)
        }
    }

}

@Composable
fun MainNavGraph(
    mainNav: MainNav = koinInject(),
    isLockTaskMode: Boolean,
    onToggleLockTask: () -> Unit,
    onEndApp: () -> Unit
) {
    // The controller
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = MainRoute
    ) {
        composable<MainRoute> {
            MainScreen(
                isLockTaskMode = isLockTaskMode,
                onToggleLockTask = onToggleLockTask,
                onEndApp = onEndApp
            )
        }
        
        composable<QRScanRoute> {
            CameraView()
        }
    }

    LaunchedEffect(Unit) {
        timber.log.Timber.d("MainActivity -> LaunchedEffect(Unit)")
        mainNav.mainNavEvents.collect { event ->
            timber.log.Timber.d("mainNavEvent $event")
            event.block.invoke(navHostController)
        }
    }
} 