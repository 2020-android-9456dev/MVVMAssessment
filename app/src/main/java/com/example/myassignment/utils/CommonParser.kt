package com.example.myassignment.utils

import com.example.myassignment.BuildConfig
import com.google.gson.Gson
import java.lang.reflect.Type

class CommonParser(private val mapper: Gson = Gson()) {

    fun <T> parseString(json: String, classOfT: Class<T>): T {
        try {
            return mapper.fromJson<T>(json, classOfT as Type)
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) e.stackTrace
        }

        return classOfT.newInstance()
    }

}
