package com.neptuneg.usecase.inputport

interface Sample {
    data class Message(val message: String)
    fun foobar(): Message
}
