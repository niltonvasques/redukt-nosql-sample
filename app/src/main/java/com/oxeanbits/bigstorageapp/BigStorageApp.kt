package com.oxeanbits.bigstorageapp

import android.app.Application
import com.github.raulccabreu.redukt.Redukt

class BigStorageApp : Application() {
    companion object {
        val redukt: Redukt<AppState> = Redukt(AppState(), true)
        val storage: Storage = Storage()
    }
    override fun onCreate() {
        super.onCreate()
        redukt.reducers["counter"] = AppStateReducer()

        redukt.middlewares["storage"] = StorageMiddleware()
    }

    override fun onTerminate() {
        super.onTerminate()
        redukt.stop()
    }
}
