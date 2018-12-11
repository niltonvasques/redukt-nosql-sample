package com.oxeanbits.bigstorageapp

class Storage {
    val pageSize = 100
    val items = mutableListOf<Request>()

    fun save(list: List<Request>) {
        items.addAll(list)
    }

    fun fetch(page: Int): List<Request> {
        val fromIndex = Math.min((page-1) * pageSize, items.size - 1)
        val toIndex = Math.min((page * pageSize) - 1, items.size - 1)
        return items.subList(fromIndex, toIndex).toList()
    }
}
