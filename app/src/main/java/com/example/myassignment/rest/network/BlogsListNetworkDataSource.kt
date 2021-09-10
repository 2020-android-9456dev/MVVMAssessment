

package com.example.myassignment.network

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.rest.network.ApiService
import com.example.myassignment.utils.CustomError
import com.example.myassignment.utils.CustomResult
import java.net.SocketTimeoutException


class BlogsListNetworkDataSource(
        val context: Context,
        private val networkService: ApiService
) : BlogslistDataSourceInterface, BaseRepository() {

    private val _result = MutableLiveData<CustomResult<BlogResModel>>()
    override val result: LiveData<CustomResult<BlogResModel>>
        get() = _result

    override suspend fun getBlogsList(limit: String, lastPageNumber: Int) {
        try {
            val downloadStatements = safeApiCall(
                call = { networkService.getBlogList(limit, lastPageNumber) },
                errorMessage = "Error fetching statements")
            println("RESPONSE LIST ITEM SIZE : "+downloadStatements.toString())
            _result.postValue(downloadStatements)
        } catch (e : SocketTimeoutException){
            Log.e("Connectivity_error", "Slow Internet Connection", e)
            _result.postValue(CustomResult.error(CustomError("Internet connection Error"), null))
        } catch (e : Exception){
            Log.e("Connectivity_error", "Internet Connection Error", e)
            _result.postValue(CustomResult.error(CustomError("Internet connection Error"), null))
        }
    }
}