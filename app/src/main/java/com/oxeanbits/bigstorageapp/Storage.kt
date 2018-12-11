package com.oxeanbits.bigstorageapp

class Storage {
    val pageSize: Long = 100

    fun save(list: List<Request>) {
        val requestBox = BigStorageApp.boxStore.boxFor(Request::class.java)
        requestBox.put(list)

        println("Storage.save")
        println(list)
    }

    fun fetch(page: Int): List<Request> {
        val requestBox = BigStorageApp.boxStore.boxFor(Request::class.java)
        return requestBox.query().build().find((page - 1) * pageSize, pageSize)
    }

    fun total(): Long {
        val requestBox = BigStorageApp.boxStore.boxFor(Request::class.java)
        return requestBox.count()
    }

    fun clear() {
        val requestBox = BigStorageApp.boxStore.boxFor(Request::class.java)
        requestBox.removeAll()
    }
}
