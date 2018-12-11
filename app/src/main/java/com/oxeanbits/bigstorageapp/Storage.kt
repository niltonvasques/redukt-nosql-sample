package com.oxeanbits.bigstorageapp

class Storage {
    fun save(list: List<Request>) {
        val requestBox = BigStorageApp.boxStore.boxFor(Request::class.java)
        requestBox.put(list)

        println("Storage.save")
        println(list)
    }

    fun fetch(page: Int, pageSize: Long = 100, sort: Boolean = false): List<Request> {
        val requestBox = BigStorageApp.boxStore.boxFor(Request::class.java)
        var query = requestBox.query()
        if (sort) query = query.order(Request_.reason)
        return query.build().find((page - 1) * pageSize, pageSize)
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
