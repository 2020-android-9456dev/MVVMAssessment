
package com.example.myassignment.network

import com.example.myassignment.utils.CommonParser
import java.io.*

class RestApiResponse<T>(
        val data: T? = null,
        val code: Int? = null,
        val message: String? = null,
        val inputStream: InputStream? = null,
        val body: String? = null,
        val headers: Map<String, List<String>>? = null) {

    fun isSuccessful(): Boolean {
        if (code != null) {
            return code in 200..299
        }

        return false
    }

    data class Builder<T>(
            var parseTo: Class<T>? = null,
            var code: Int? = 0,
            var message: String? = "",
            var inputStream: InputStream? = null,
            var headers: Map<String, List<String>> = emptyMap()) {
        private var streamClosed = false
        fun parseTo(parseTo: Class<T>) = apply { this.parseTo = parseTo }
        fun code(code: Int) = apply { this.code = code }
        fun message(message: String) = apply { this.message = message }
        fun inputStream(inputStream: InputStream?) = apply { this.inputStream = inputStream }
        fun headers(headers: Map<String, List<String>>) = apply { this.headers = headers }

        fun build() = RestApiResponse(
                parse(inputStream, parseTo),
                code,
                message,
                inputStream,
                readStream(if (streamClosed) null else BufferedInputStream(inputStream)),
                headers)

        private fun <T> parse(inputStream: InputStream?, classOfT: Class<T>?): T? {
            if (inputStream == null || classOfT == null) return classOfT?.newInstance()

            val string = readStream(BufferedInputStream(inputStream))
            streamClosed = true
            return CommonParser().parseString(string, classOfT)
        }

        private fun readStream(`in`: InputStream?): String {
            if (`in` == null) {
                return ""
            }

            var reader: BufferedReader? = null
            val displayMessage = StringBuffer()
            try {
                reader = BufferedReader(InputStreamReader(`in`))
                var line: String? = null
                while ({ line = reader.readLine(); line }() != null) {
                    displayMessage.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return displayMessage.toString()
        }
    }
}