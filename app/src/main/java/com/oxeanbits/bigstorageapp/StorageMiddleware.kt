package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.middlewares.AfterAction
import com.github.raulccabreu.redukt.middlewares.AfterActions
import com.github.raulccabreu.redukt.middlewares.BaseAnnotatedMiddleware
import com.github.raulccabreu.redukt.middlewares.BeforeAction

data class PagePayload (
    val requests: List<Request> = emptyList(),
    val total: Long = 0
)

class StorageMiddleware : BaseAnnotatedMiddleware<AppState>() {

    @AfterActions(["INC", "DEC", "SORT", "PAGE_SIZE"])
    fun incAction(state: AppState, action: Action<Any>) {
        if (state.items.size > state.pageSize) {
            BigStorageApp.redukt.dispatch(Action("LOW_MEMORY", null))
        }
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }

    @BeforeAction("REFRESH_REQUEST")
    fun refresh(state: AppState, action: Action<Any>) {
        val page = BigStorageApp.storage.fetch(state.page, state.pageSize, state.sort == "ASC")
        BigStorageApp.redukt.dispatch(Action("REFRESH", PagePayload(page, BigStorageApp.storage.total())))
    }

    @BeforeAction("CLEAR")
    fun clear(state: AppState, action: Action<Any>) {
        BigStorageApp.storage.clear()
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }

    @AfterAction("DIRTY")
    fun dirty(state: AppState, action: Action<Any>) {
        BigStorageApp.storage.save(state.items)
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }

    @AfterAction("REMOVE")
    fun remove(state: AppState, action: Action<Long>) {
        action.payload?.let { BigStorageApp.storage.remove(it) }
    }
}
