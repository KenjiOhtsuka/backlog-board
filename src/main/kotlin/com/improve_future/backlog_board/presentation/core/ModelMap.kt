package com.improve_future.backlog_board.presentation.core

import org.springframework.ui.Model

class ModelMap(model: Model) {
    val map: Map<String, Any?>
    init {
        map = model.asMap()
    }

    fun hasKey(key: String): Boolean {
        return map.containsKey(key)
    }

    fun getMessageMap(): MessageMap {
        if (hasKey("messages"))
            return map["messages"] as MessageMap
        return MessageMap()
    }

    fun getSuccessMessageList(): List<Message> {
        return getMessageMap().getSuccessMessages()
    }

    fun getErrorMessageList(): List<Message> {
        return getMessageMap().getErrorMessages()
    }
}