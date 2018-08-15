package com.improve_future.backlog_board.presentation.common

object LayoutJsonView {
    fun success(data: Map<String, Any?>? = null): Map<String, Any?> {
        return mapOf("data" to data)
    }

    fun error(code: Long, messageList: List<String>): Map<String, Any?> {
        return mapOf(
                "error" to mapOf(
                        "code" to code,
                        "messages" to messageList
                )
        )
    }
}