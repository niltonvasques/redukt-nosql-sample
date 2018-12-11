package com.oxeanbits.bigstorageapp

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.LinearLayout
import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.states.StateListener
import com.oxeanbits.bigstorageapp.BigStorageApp.Companion.redukt
import trikita.anvil.Anvil
import trikita.anvil.BaseDSL
import trikita.anvil.DSL.*
import trikita.anvil.RenderableView

class MainActivity : AppCompatActivity(), StateListener<AppState> {
    private var state: AppState = BigStorageApp.redukt.state

    override fun onChanged(state: AppState) {
        println(state)
        this.state = state
        Anvil.render()
    }

    override fun hasChanged(newState: AppState, oldState: AppState): Boolean {
        return newState != oldState
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(object : RenderableView(this) {
            override fun view() {
                linearLayout {
                    orientation(LinearLayout.VERTICAL)
                    textView {
                        text("!!! LOW MEMORY !!!")
                        gravity(BaseDSL.CENTER)
                        backgroundColor(Color.RED)
                        visibility(state.lowMemory)
                    }
                    textView {
                        text("Items on memory: ${state.items.size} | Items on database: ${state.count}")
                    }
                    button {
                        text("FETCH!")
                        onClick { redukt.dispatch(Action<Any>("FETCH_ITEMS")) }
                    }
                    linearLayout {
                        button {
                            text("<")
                            onClick { redukt.dispatch(Action<Any>("DEC")) }
                        }
                        textView {
                            text("page: ${state.page}")
                        }
                        button {
                            text(">")
                            onClick { redukt.dispatch(Action<Any>("INC")) }
                        }
                        button {
                            text(state.sort)
                            onClick { redukt.dispatch(Action<Any>("SORT")) }
                        }
                        button {
                            text("clear")
                            onClick { redukt.dispatch(Action<Any>("CLEAR")) }
                        }
                    }
                    editText {
                        text(state.pageSize.toString())
                        inputType(InputType.TYPE_CLASS_NUMBER)
                        onTextChanged {
                            if (it.isNotEmpty()) redukt.dispatch(Action<Int>("PAGE_SIZE", it.toString().toInt()))
                        }
                    }
                    textView {
                        text(state.items.toString())
                    }
                }
            }
        })
        redukt.listeners.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
