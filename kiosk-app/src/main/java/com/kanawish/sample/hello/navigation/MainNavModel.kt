package com.kanawish.sample.hello.navigation

import androidx.navigation.NavHostController
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/**
 * These will be consumed by the NavHostController.
 */
data class NavEvent(val block: NavHostController.() -> Unit)


interface MainNav {
    val mainNavEvents: SharedFlow<NavEvent>
    fun nav(route: TypeRoute)
    fun navUp()
    fun navBack(route:TypeRoute, inclusive: Boolean = true)
    fun navEvent(block: NavHostController.()->Unit)
}

/**
 * Useful to support current version of NavGraphBuilder.bottomSheet
 * builder that only accepting strings as routes.
 */
interface NamedRoute {
    val name: String get() = this::class.simpleName!!
}

/** When type routes are used, remember the implementation needs to be @Serializable. */
sealed class TypeRoute {
    @Serializable data object MainRoute: TypeRoute()
    @Serializable data object QRScanRoute: TypeRoute()
}

class MainNavModel(scope: CoroutineScope) : MainNav, CoroutineScope by scope {
    private val _mainNavEvents = MutableSharedFlow<NavEvent>()
    // NavHostController will receive and handle nav events sent over this shared flow.
    override val mainNavEvents = _mainNavEvents.asSharedFlow()

    override fun nav(route: TypeRoute) {
        navEvent {
            Timber.d { "navigate(${route::class.simpleName})" }
            navigate(route)
        }
    }

    override fun navUp() {
        navEvent {
            Timber.d { "navigateUp()" }
            navigateUp()
        }
    }

    override fun navBack(
        route: TypeRoute,
        inclusive: Boolean
    ) {
        navEvent {
            Timber.d { "popBackstack(${route::class.simpleName})" }
            popBackStack(route, inclusive)
        }
    }

    /**
     * We emit nav events onto the shared flow with this function.
     */
    override fun navEvent(block: NavHostController.() -> Unit) {
        launch {
            _mainNavEvents.emit(NavEvent(block))
        }
    }
}