package com.oxeanbits.bigstorageapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.raulccabreu.redukt.Redukt
import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.states.StateListener
import trikita.anvil.Anvil
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class ScrollingActivity : AppCompatActivity(), StateListener<Int> {
    private var state: Int = 0

    override fun onChanged(state: Int) {
        this.state = state
        Anvil.render()
    }

    override fun hasChanged(newState: Int, oldState: Int): Boolean {
        return newState != oldState
    }

    private val redukt: Redukt<Int> = Redukt<Int>(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(object : RenderableView(this) {
            override fun view() {
                linearLayout {
                    textView {
                        text("Clicks: $state")
                    }
                    button {
                        text("Click me!")
                        onClick { redukt.dispatch(Action("INC", null)) }
                    }
                }
            }
        })
        redukt.reducers["counter"] = CounterReducer()
        redukt.listeners.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        redukt.stop()
    }
}
