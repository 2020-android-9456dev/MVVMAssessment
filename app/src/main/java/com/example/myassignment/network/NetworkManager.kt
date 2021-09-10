

package com.example.myassignment.network

import android.util.Log
import com.example.myassignment.BuildConfig
import com.google.gson.Gson
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.io.Writer
import java.net.HttpURLConnection
import java.net.URL

class NetworkManager<T>(val networkModel: NetworkManagerModel) {

    companion object {
        const val READ_TIMEOUT = 10000
        const val CONNECTION_TIMEOUT = 10000
    }

    fun execute(classOfType: Class<T>): RestApiResponse<T> {
        try {
            val url = URL((networkModel.base + networkModel.path + networkModel.endPoint))
            println("URL : ----> "+url)
            val connection = url.openConnection() as HttpURLConnection

            when (networkModel.method) {
                NetworkManagerMethods.GET -> {
                    connection.requestMethod = "GET"
                }
                NetworkManagerMethods.POST -> {

                }
                else ->
                    print("TODO unsupported networkModel.method")
            }

            if (connection.inputStream == null) {
                Log.i("Response ", connection.responseMessage)
                throw java.lang.Exception("input stream is null")
            } else {
                Log.i("Response", connection.responseMessage)
                return RestApiResponse.Builder<T>()
                        .parseTo(classOfType)
                        .code(connection.responseCode)
                        .inputStream(connection.inputStream)
                        .headers(connection.headerFields)
                        .message(connection.responseMessage)
                        .build()
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }

            return RestApiResponse.Builder<T>().message(e.cause?.localizedMessage ?: "").build()
        }
    }

}