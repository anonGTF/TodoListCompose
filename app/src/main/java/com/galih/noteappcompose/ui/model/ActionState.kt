package com.galih.noteappcompose.ui.model

import de.palm.composestateevents.StateEvent
import de.palm.composestateevents.StateEventWithContent
import de.palm.composestateevents.consumed

data class ActionState(
    val isLoading: Boolean = false,
    val eventSucceed: StateEvent = consumed,
    val eventFailed: StateEventWithContent<String> = consumed()
)
