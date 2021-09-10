package com.example.myassignment.rest.network

import com.example.myassignment.network.ApiServiceInterface
import com.example.myassignment.network.NetworkManagerMethods
import com.example.myassignment.rest.enum.ServiceEndPoint
import com.example.myassignment.rest.enum.ServicePath
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.utils.AppConfiguration

class ApiService : ApiServiceInterface {
    fun getBlogList(limit: String, page: Int = 0) = execute(BlogResModel::class.java, AppConfiguration.defaultConfig().baseURL, ServicePath.MobileAppServices.displayName, ServiceEndPoint.BlogList.displayName+"?page="+page+"&limit="+limit, NetworkManagerMethods.GET, null)
}