package com.improve_future.backlog_board.presentation.core

data class Link(
        var path: String,
        var params: Map<String, Any?> = mapOf(),
        var ssl: Boolean = true,
        var domain: String = "") {
    fun composePath(): String {
        val parameterSet = mutableSetOf<String>()
        for ((key, value) in params) {
            if (value == null || value.toString().isEmpty()) continue
            when(value) {
                is Collection<Any?> -> {
                    if (value.isNotEmpty())
                        parameterSet.add(
                                value.
                                    filter { it != null } .
                                    map { key + "=" + it.toString() } .
                                    joinToString("&"))
                }
                is Array<*> -> {
                    value.forEach {
                        parameterSet.add(key + "=" + it.toString())
                    }
                }
                else -> {
                    parameterSet.add(key + "=" + value.toString())
                }
            }
        }
        if (parameterSet.isNotEmpty())
            return path + "?" + parameterSet.joinToString("&")
        return path
    }

    fun composeUrl(): String {
        var url: String
        if (ssl) url = "https://"
        else url = "http://"
        url += domain + composePath()
        return url
    }

    override fun toString(): String {
        return composePath()
    }
}