package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.middlewares.AfterAction
import com.github.raulccabreu.redukt.middlewares.AfterActions
import com.github.raulccabreu.redukt.middlewares.BaseAnnotatedMiddleware
import com.github.raulccabreu.redukt.middlewares.BeforeAction
import com.mooveit.library.Fakeit

data class PagePayload (
    val requests: List<Request> = emptyList(),
    val total: Long = 0
)

class StorageMiddleware : BaseAnnotatedMiddleware<AppState>() {
    var count = 0

    @AfterActions(["INC", "DEC", "SORT", "PAGE_SIZE"])
    fun incAction(state: AppState, action: Action<Any>) {
        if (state.items.size > state.pageSize) {
            BigStorageApp.redukt.dispatch(Action("LOW_MEMORY", null))
        }
        BigStorageApp.redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }

    @BeforeAction("FETCH_ITEMS")
    fun fetchItems(state: AppState, action: Action<Any>) {
        val newList = state.items.toMutableList()
//        for (i in 1..(Math.pow(2.toDouble(), count.toDouble()).toInt())) {
        for (i in 1..100) {
            newList.add(Request(0, Fakeit.business().name()))
        }
        count++
        BigStorageApp.storage.save(newList)
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
}
