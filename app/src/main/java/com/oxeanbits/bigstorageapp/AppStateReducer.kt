package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Reduce
import com.github.raulccabreu.redukt.reducers.BaseAnnotatedReducer

class AppStateReducer : BaseAnnotatedReducer<AppState>() {
    @Reduce("INC")
    fun inc(state: AppState, empty: Any?): AppState {
        return state.copy(page = state.page + 1)
    }

    @Reduce("DEC")
    fun dec(state: AppState, empty: Any?): AppState {
        return state.copy(page = Math.max(state.page - 1, 1))
    }

    @Reduce("LOW_MEMORY")
    fun lowMemoery(state: AppState, empty: Any?): AppState {
        return state.copy(lowMemory = true)
    }

    @Reduce("REFRESH")
    fun refresh(state: AppState, list: List<Request>): AppState {
        val newState = state.copy(items = list)
        println("REFRESH...")
        println(newState)
        return newState
    }
}
