package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.middlewares.BaseAnnotatedMiddleware
import com.github.raulccabreu.redukt.middlewares.BeforeAction
import com.mooveit.library.Fakeit
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

    @BeforeAction("FETCH_ITEMS")
    fun fetchItems(state: AppState, action: Action<Any>) {
        val newList = state.items.toMutableList()
//        for (i in 1..(Math.pow(2.toDouble(), count.toDouble()).toInt())) {
        for (i in 1..100) {
            newList.add(Request(0, Fakeit.business().name()))
        }
        BigStorageApp.storage.save(newList)
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }

    @BeforeAction("FETCH_MANY_ITEMS")
    fun fetchManyItems(state: AppState, action: Action<Any>) {
        val newList = state.items.toMutableList()
        for (i in 1..5000) {
            newList.add(Request(0, Fakeit.business().name()))
        }
        BigStorageApp.storage.save(newList)
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }
}
