package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.middlewares.AfterActions
import com.github.raulccabreu.redukt.middlewares.BaseAnnotatedMiddleware
import com.github.raulccabreu.redukt.middlewares.BeforeAction

class StorageMiddleware : BaseAnnotatedMiddleware<AppState>() {
    var count = 0

    @AfterActions(["INC", "DEC"])
    fun incAction(state: AppState, action: Action<Any>) {
        if (state.items.size > state.pageSize) {
            BigStorageApp.redukt.dispatch(Action("LOW_MEMORY", null))
        }
        val page = BigStorageApp.storage.fetch(state.page)
        BigStorageApp.redukt.dispatch(Action("REFRESH", page))
    }

    @BeforeAction("FETCH_ITEMS")
    fun fetchItems(state: AppState, action: Action<Any>) {
        val newList = state.items.toMutableList()
        for (i in 1..(Math.pow(2.toDouble(), count.toDouble()).toInt())) {
            newList.add(Request(i, "Heat ${i * count}"))
        }
        count++
        BigStorageApp.storage.save(newList)
        val page = BigStorageApp.storage.fetch(state.page)
        BigStorageApp.redukt.dispatch(Action("REFRESH", page))
    }
}
