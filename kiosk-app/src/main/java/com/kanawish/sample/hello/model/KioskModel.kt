package com.kanawish.sample.hello.model

import android.app.ActivityManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

enum class ActivityTaskEvent {
    LOCK, UNLOCK, END_APP
}

class KioskModel(
    val activityManager: ActivityManager,
    scope: CoroutineScope
) : CoroutineScope by scope {

    data class State(
        val unlockedTaskMode: Boolean
    )

    private val _activityTaskEvent = MutableSharedFlow<ActivityTaskEvent>()
    val activityTaskEvent: SharedFlow<ActivityTaskEvent> = _activityTaskEvent.asSharedFlow()

    private val _state = MutableStateFlow(State(isUnlockedTaskMode()))
    val state: StateFlow<State> = _state.asStateFlow()

    // Actually checks Android lockTaskModeState
    private fun isUnlockedTaskMode(): Boolean = activityManager.lockTaskModeState == ActivityManager.LOCK_TASK_MODE_NONE

    init {
        Timber.d("Initializing KioskViewModel ${this@KioskModel}")
        launch {
            activityTaskEvent.collect { event ->
                Timber.d("🐇🕳️ Received activity task event: $event / ${this@KioskModel}")
            }
        }
    }

    fun toggleLockTaskMode() {
        if( isUnlockedTaskMode() != state.value.unlockedTaskMode ) {
            Timber.w("Unlocked task mode mismatch detected, won't toggle, but updating state to reflect current task mode")
//            _state.value = State(unlockedTaskMode = isUnlockedTaskMode())
        }

        launch {
            if( state.value.unlockedTaskMode ) {
                Timber.d("🔒Locking task mode")
                _activityTaskEvent.emit(ActivityTaskEvent.LOCK)
            } else {
                Timber.d("🔓Unlocking task mode")
                _activityTaskEvent.emit(ActivityTaskEvent.UNLOCK)
            }
            // We trust that the change will have taken place in the system by now.
//            _state.update { it.copy(unlockedTaskMode = !it.unlockedTaskMode) }
        }
    }

    fun endApp() {
        Timber.d("Ending app triggering")
        launch {
            _activityTaskEvent.emit(ActivityTaskEvent.END_APP)
        }
    }
}