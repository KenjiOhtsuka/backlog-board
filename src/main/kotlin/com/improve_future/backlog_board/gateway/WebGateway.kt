package com.improve_future.backlog_board.gateway

import com.improve_future.backlog_board.utility.JsonUtility
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

// ToDo: refactor
object WebGateway {
    private fun buildConnection(
            url: URL, payload: Any, method: String, isJson: Boolean): HttpURLConnection {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = method
        connection.doOutput = true
        if (payload is JSONObject || payload is Map<*, *> || isJson)
            setUpHeaderForJson(connection)
        return connection
    }

    private fun setUpHeaderForJson(connection: HttpURLConnection):
            HttpURLConnection {
        connection.setRequestProperty(
                "Content-Type",
                "application/json; charset=UTF-8")
        connection.setRequestProperty(
                "Accept",
                "application/json")
        return connection
    }

    /**
     * @param url
     * @param payload should be String or JSONObject
     * @return String
     */
    fun post(url: URL, payload: Any, isJson: Boolean = false): String {
        return post(url, payload.toString())
    }

    fun get(url: String, params: Map<String, Any>): Any {
        return get(URL(url), params)
    }

    fun get(url: URL, params: Map<String, Any>): Any {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.doOutput = true

        val rd = BufferedReader(InputStreamReader(connection.inputStream))
        val resultBytes = connection.inputStream.readBytes()
        rd.close()
        return resultBytes
    }

    fun getImage(url: URL, params: Map<String, Any>): ByteArray {
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.doOutput = true

        val rd = BufferedReader(InputStreamReader(connection.inputStream))
        val resultBytes = connection.inputStream.readBytes()
        rd.close()
        return resultBytes
    }

    fun post(url: String, payload: String, isJson: Boolean = false): String {
        return post(URL(url), payload)
    }

    fun post(url: URL, payload: String, isJson: Boolean = false): String {
        val connection = buildConnection(url, payload, "POST", isJson)
        connection.connect()

        val os = connection.outputStream
        os.write(payload.toByteArray())
        os.close()

        if (connection.responseCode != HttpsURLConnection.HTTP_OK) {
            throw Exception(connection.responseCode.toString() + ":" +
                    connection.responseMessage) }

        val reader = BufferedReader(
                InputStreamReader(connection.inputStream, "UTF-8"))
        val sb = StringBuilder()
        do {
            val line = reader.readLine() ?: break
            sb.appendln(line)
        } while (true)

        connection.disconnect()

        return sb.toString()
    }

    fun postJson(url: URL, payload: Map<String, Any?>):
            JSONObject {
        val resultString = post(
                url, JsonUtility.convertMapToJsonObject(payload).toString(), true)

        return JSONObject(resultString)
    }

    fun postJson(url: String, payload: Map<String, Any?>): JSONObject {
        return postJson(URL(url), payload)
    }

    fun postJson(url: String, payload: String): JSONObject {
        val resultString = post(URL(url), payload, true)

        return JSONObject(resultString)
    }
}