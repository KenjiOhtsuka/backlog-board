package com.improve_future.backlog_board.base

import kotlin.reflect.KProperty

abstract class Master(vararg values: Value) {
    data class Value(
            val id: Long,
            val key:String,
            val enName: String,
            private val _jaName: String? = null,
            private val _enDescription: String? = null,
            private val _jaDescription: String? = null) {

        val jaName: String
        val enDescription: String
        val jaDescription: String

        init {
            if (!this.key.matches(Regex("^[a-z0-9_]+$")))
                throw Exception(
                        "key value \"$key\" is not valid.")
            jaName = _jaName ?: enName
            enDescription = _enDescription ?: enName
            jaDescription = _jaDescription ?: jaName
        }
        fun name(locale: String = "en"): String {
            if (locale == "ja") return this.jaName
            return this.enName
        }
        fun description(locale: String = "ja"): String {
            if (locale == "ja") return this.jaDescription
            return this.enDescription
        }
    }

    init {
        // ToDo: Throw error there is any duplication about id and key
        values.forEach {
        }
    }

    private val idMap: Map<Long, Value> by lazy {
        val map = mutableMapOf<Long, Value>()
        values.forEach { map.put(it.id, it) }
        map
    }

    private val keyMap: Map<String, Value> by lazy {
        val map = mutableMapOf<String, Value>()
        values.forEach { map.put(it.key, it) }
        map
    }

    val keys: Set<String>
    get() { return this.keys }

    fun value(id: Long): Value? {
        return this.idMap[id]
    }

    fun value(key: String): Value? {
        return this.keyMap[key]
    }
}