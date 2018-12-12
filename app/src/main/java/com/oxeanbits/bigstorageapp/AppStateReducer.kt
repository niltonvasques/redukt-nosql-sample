package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Reduce
import com.github.raulccabreu.redukt.reducers.BaseAnnotatedReducer

class AppStateReducer : BaseAnnotatedReducer<AppState>() {
    @Reduce("DIRTY")
    fun dirty(state: AppState, id: Long): AppState {
        val index = state.items.indexOfFirst { it.id == id }
        if (index == -1) return state
        val items = state.items.toMutableList()
        items[index] = items[index].copy(dirty = true)
        return state.copy(items = items)
    }

    @Reduce("INC")
    fun inc(state: AppState, empty: Any?): AppState {
        return state.copy(page = state.page + 1)
    }

    @Reduce("DEC")
    fun dec(state: AppState, empty: Any?): AppState {
        return state.copy(page = Math.max(state.page - 1, 1))
    }

    @Reduce("SORT")
    fun sort(state: AppState, empty: Any?): AppState {
        var sort = "NONE"
        if (state.sort == "NONE") sort = "ASC"
        return state.copy(sort = sort)
    }

    @Reduce("LOW_MEMORY")
    fun lowMemoery(state: AppState, empty: Any?): AppState {
        return state.copy(lowMemory = true)
    }

    @Reduce("REFRESH")
    fun refresh(state: AppState, payload: PagePayload): AppState {
        val newState = state.copy(items = payload.requests, count = payload.total)
        return newState
    }

    @Reduce("PAGE_SIZE")
    fun pageSize(state: AppState, pageSize: Long): AppState {
        val size = Math.max(1, Math.min(pageSize, 100))
        return state.copy(pageSize = size)
    }

    @Reduce("SYNC")
    fun sync(state: AppState, empty: Any?): AppState {
        return state.copy(sync = true)
    }

    @Reduce("SYNC_END")
    fun syncEnd(state: AppState, empty: Any?): AppState {
        return state.copy(sync = false)
    }
}
