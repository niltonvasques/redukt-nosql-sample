package com.oxeanbits.bigstorageapp

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.github.raulccabreu.redukt.actions.Action
import com.github.raulccabreu.redukt.states.StateListener
import com.oxeanbits.bigstorageapp.BigStorageApp.Companion.redukt
import trikita.anvil.*
import trikita.anvil.DSL.*

class MainActivity : AppCompatActivity(), StateListener<AppState> {
    private var state: AppState = BigStorageApp.redukt.state
    private val items = mutableListOf<Request>()

    val requestsAdapter = RenderableAdapter.withItems(items) { pos, item ->
        relativeLayout {
            BaseDSL.size(BaseDSL.MATCH, BaseDSL.dip(50))
            textView {
                text("#0${item.id} - ${item.reason}${if (item.dirty) " (dirty)" else ""}")
                BaseDSL.textSize(BaseDSL.sip(20.toFloat()))
                textColor(Color.HSVToColor(floatArrayOf(pos*3.6f, 1f, 1f)))
                BaseDSL.centerVertical()
            }
            linearLayout {
                BaseDSL.alignParentRight()
                button {
                    text("dirty")
                    visibility(!item.dirty)
                    onClick { redukt.dispatch(Action<Long>("DIRTY", item.id)) }
                }
                button {
                    text("REMOVE")
                    onClick { redukt.dispatch(Action<Long>("REMOVE", item.id)) }
                }
            }
        }
    }

    override fun onChanged(state: AppState) {
        println(state)
        this.state = state
        runOnUiThread {
            items.clear()
            items.addAll(state.items)
            requestsAdapter.notifyDataSetChanged()
            Anvil.render()
        }
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
                    horizontalProgressBar {
                        BaseDSL.size(BaseDSL.MATCH, BaseDSL.WRAP)
                        visibility(state.sync)
                        indeterminate(true)
                    }
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
                    button {
                        text("FETCH 2^n items")
                        onClick { redukt.dispatch(Action<Any>("FETCH_MANY_ITEMS")) }
                    }
                    button {
                        text("sync")
                        onClick { redukt.dispatch(Action<Any>("SYNC")) }
                    }
                    linearLayout {
                        button {
                            text("<")
                            BaseDSL.size(dip(50), BaseDSL.WRAP)
                            onClick { redukt.dispatch(Action<Any>("DEC")) }
                        }
                        textView {
                            text("page: ${state.page}")
                        }
                        button {
                            text(">")
                            BaseDSL.size(dip(50), BaseDSL.WRAP)
                            onClick { redukt.dispatch(Action<Any>("INC")) }
                        }
                        editText {
                            text(state.pageSize.toString())
                            inputType(InputType.TYPE_CLASS_NUMBER)
                            onTextChanged {
                                if (it.isNotEmpty()) redukt.dispatch(Action<Int>("PAGE_SIZE", it.toString().toInt()))
                            }
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
                    listView {
                        init {
                            adapter(requestsAdapter)
                        }
                        BaseDSL.size(BaseDSL.MATCH, BaseDSL.MATCH)
                    }
                    textView {
                        text(state.items.toString())
                    }
                }
            }
        })
        redukt.listeners.add(this)
    }

    override fun onResume() {
        super.onResume()
        redukt.dispatch(Action<Any>("REFRESH_REQUEST"))
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    inline fun horizontalProgressBar(crossinline func: () -> Unit) {
        DSL.v(HorizontalProgressBar::class.java) {
            func()
        }
    }

    class HorizontalProgressBar(context: Context):
        ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)

}
