package com.oxeanbits.bigstorageapp

data class AppState (
    val items: List<Request> = ArrayList(),
    val count: Long = 0,
    val sort: String = "NONE",
    val page: Int = 1,
    val pageSize: Long = 100,
    val lowMemory: Boolean = false
)
