package com.improve_future.backlog_board

import org.json.JSONArray
import org.json.JSONObject

object JsonUtility {
    fun convertMapToJsonObject(map: Map<String, Any?>): JSONObject {
        val json = JSONObject()
        map.forEach {
            json.put(it.key, convertValueToJsonValue(it.value))
        }
        return json
    }

    private fun convertValueToJsonValue(value: Any?): Any? {
        return when (value) {
            is Number, is String, is Boolean, null -> value
            is List<Any?> -> JSONArray().put(value.map { convertValueToJsonValue(it) })
            is Map<*, *> -> convertMapToJsonObject(value as Map<String, Any?>)
            else -> throw Exception("Invalid Value.")
        }
    }
}