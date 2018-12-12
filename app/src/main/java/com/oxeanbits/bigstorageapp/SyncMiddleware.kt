package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.middlewares.AfterAction
import com.github.raulccabreu.redukt.middlewares.BaseAnnotatedMiddleware

class SyncMiddleware : BaseAnnotatedMiddleware<AppState>() {
    @AfterAction("SYNC")
    fun dirty(state: AppState, action: Action<Any>) {
        val items = BigStorageApp.storage.dirty().toMutableList()
        items.forEachIndexed { index, request ->
            items[index] = request.copy(dirty = false)
        }
        BigStorageApp.storage.save(items)
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }
}
