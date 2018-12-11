package com.oxeanbits.bigstorageapp

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Request(
    @Id var id: Long = -1,
    val reason: String = ""
)

