package com.oxeanbits.bigstorageapp

import android.app.Application
import com.github.raulccabreu.redukt.Redukt
import com.mooveit.library.Fakeit
import io.objectbox.BoxStore

class BigStorageApp : Application() {
    companion object {
        val redukt: Redukt<AppState> = Redukt(AppState(), true)
        val storage: Storage = Storage()
        lateinit var boxStore: BoxStore
    }


    override fun onCreate() {
        super.onCreate()
        Fakeit.init()

        redukt.reducers["counter"] = AppStateReducer()

        redukt.middlewares["storage"] = StorageMiddleware()
        redukt.middlewares["sync"] = SyncMiddleware()
        boxStore = MyObjectBox.builder().androidContext(this).build()
    }

    override fun onTerminate() {
        super.onTerminate()
        redukt.stop()
    }
}
