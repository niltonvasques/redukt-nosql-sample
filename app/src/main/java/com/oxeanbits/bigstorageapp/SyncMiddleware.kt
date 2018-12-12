package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.middlewares.BaseAnnotatedMiddleware
import com.github.raulccabreu.redukt.middlewares.BeforeAction
import kotlin.concurrent.thread

class SyncMiddleware : BaseAnnotatedMiddleware<AppState>() {
    @BeforeAction("SYNC")
    fun dirty(state: AppState, action: Action<Any>) {
        thread (start = true){
            Thread.sleep(2000)
            val items = BigStorageApp.storage.dirty().toMutableList()
            items.forEachIndexed { index, request ->
                items[index] = request.copy(dirty = false)
            }
            BigStorageApp.storage.save(items)
            BigStorageApp.redukt.dispatch(Action<Any>("SYNC_END"))
            BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
        }
    }
}
