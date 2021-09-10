

package com.example.myassignment.network

data class NetworkManagerModel(
    var base: String = "",
    var path: String = "",
    var endPoint: String = "",
    var postParams: HashMap<String, String>? = HashMap(),
    var headers: HashMap<String, String> = HashMap(),
    var method: NetworkManagerMethods = NetworkManagerMethods.POST
)