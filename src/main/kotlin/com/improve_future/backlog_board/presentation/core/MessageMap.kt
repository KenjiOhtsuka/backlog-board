package com.improve_future.backlog_board.presentation.core

class MessageMap {
    private val successMessages: MutableList<Message> = mutableListOf()
    private val errorMessages: MutableList<Message> = mutableListOf()

    fun addSuccessMessage(message: Message) {
        successMessages.add(message)
    }
    fun addSuccessMessage(text: String) {
        addSuccessMessage(Message(text))
    }

    fun addErrorMessage(message: Message) {
        errorMessages.add(message)
    }
    fun addErrorMessage(text: String) {
        addErrorMessage(Message(text))
    }

    fun getSuccessMessages() = successMessages.toList()
    fun getErrorMessages() = errorMessages.toList()

    fun hasMessage(): Boolean {
        return hasSuccessMessage() || hasErrorMessage()
    }

    fun hasSuccessMessage(): Boolean {
        return successMessages.count() > 0
    }

    fun hasErrorMessage(): Boolean {
        return errorMessages.count() > 0
    }
}