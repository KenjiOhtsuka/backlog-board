package com.improve_future.backlog_board.base

abstract class EnumMaster<T:MasterEnum>(val values: List<T>) {
    private val idMap: Map<Long, T> by lazy {
        val map = mutableMapOf<Long, T>()
        values.forEach { map.put(it.id, it) }
        map
    }

    private val keyMap: Map<String, T> by lazy {
        val map = mutableMapOf<String, T>()
        values.forEach { map.put(it.name, it) }
        map
    }

    val keys: Set<String>
        get() { return this.keyMap.keys }

    fun value(id: Long?): T? {
        return this.idMap[id]
    }

    fun value(key: String?): T? {
        return this.keyMap[key]
    }

    fun hasId(id: Long): Boolean {
        return idMap.keys.contains(id)
    }

    fun hasKey(key: String): Boolean {
        return keyMap.keys.contains(key)
    }
}