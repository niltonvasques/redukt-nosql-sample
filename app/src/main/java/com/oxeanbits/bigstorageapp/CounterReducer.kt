package com.oxeanbits.bigstorageapp

import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.reducers.Reducer

class CounterReducer : Reducer<Int> {
    override fun reduce(state: Int, action: Action<*>): Int {
        if (action.name == "INC") return state + 1
        if (action.name == "DEC") return state - 1
        return state
    }
}
