package com.oxeanbits.bigstorageapp

class Storage {
    val pageSize = 100
    val items = mutableListOf<String>()

    fun save(list: List<String>) {
        items.addAll(list)
    }

    fun fetch(page: Int): List<String> {
        val fromIndex = Math.min((page-1) * pageSize, items.size - 1)
        val toIndex = Math.min((page * pageSize) - 1, items.size - 1)
        return items.subList(fromIndex, toIndex).toList()
    }
}
