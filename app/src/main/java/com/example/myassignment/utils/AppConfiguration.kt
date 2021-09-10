

package com.example.myassignment.utils

import java.util.*

class  AppConfiguration {

    /*
    true -> For run application without network call
    false -> For run application with network call
     */
    var useLocalWebServices = false

    /*
    baseURL -> This base url required when application calling produlction/stub webservices
     */
    var baseURL = "http://5e99a9b1bc561b0016af3540.mockapi.io/"


    companion object {

        //static reference for singleton
        private var _instance: AppConfiguration = AppConfiguration()

        //returning the reference
        @Synchronized
        fun defaultConfig(): AppConfiguration {
            return _instance
        }
    }
}