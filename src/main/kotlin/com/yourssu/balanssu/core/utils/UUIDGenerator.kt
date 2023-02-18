package com.yourssu.balanssu.core.utils

import com.fasterxml.uuid.Generators

object UUIDGenerator {
    fun generateUUID() =
        Generators.timeBasedGenerator().generate().toString()
}