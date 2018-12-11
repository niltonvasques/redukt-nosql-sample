package com.oxeanbits.bigstorageapp

data class AppState (
    val items: List<String> = ArrayList(),
    val count: Int = 0,
    val page: Int = 1,
    val pageSize: Int = 100,
    val lowMemory: Boolean = false
)
