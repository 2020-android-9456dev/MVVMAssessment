

package com.example.myassignment.network

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myassignment.presentables.LocalFilePresentable
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.utils.CommonParser
import com.example.myassignment.utils.CustomResult
import com.example.myassignment.utils.CustomResultStatus
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import java.io.File
import java.io.IOException


class BlogsListLocalDataSource(private val assetManager: AssetManager,
                               private val mapFileDescriptor: Map<Int, AssetFileDescriptor>? = HashMap(),
                               private val mapFileForTests: Map<Int, File>? = HashMap()
) : BlogslistDataSourceInterface, LocalFilePresentable {

    private val _result = MutableLiveData<CustomResult<BlogResModel>>()
    override val result: LiveData<CustomResult<BlogResModel>> = _result

    override suspend fun getBlogsList(limit: String, lastPageNumber: Int) {
        try {
            // Set a timeout - useful for testing animations
            delay(FAKE_RESPONSE_TIME)

            val fetchedData = getLocalDataAsync(lastPageNumber).await()

            if (fetchedData.status == CustomResultStatus.ERROR) {
                throw Exception(fetchedData.error?.message)
            }

            _result.postValue(CustomResult.success(fetchedData.data))
        } catch (e: IOException) {
            _result.postValue(CustomResult.error("Error loading the local model", null, e))
        } catch (e: Exception) {
            Log.e("Other errors", "getLocalModel error", e)
            _result.postValue(CustomResult.error("Error loading the local model", null, e))
        }
    }



    /**
     * getLocalDataAsync : It read mock data json file in respect of required response model
     */
    private fun getLocalDataAsync(lastPageNumber: Int) = GlobalScope.async {
        if (mapFileDescriptor?.containsKey(lastPageNumber) == true) {
            getLocalModel(CommonParser(), mapFileDescriptor?.get(lastPageNumber), null, BlogResModel::class.java)
        }
        else if (mapFileForTests?.containsKey(lastPageNumber) == true) {
            getLocalModel(CommonParser(), null, mapFileForTests?.get(lastPageNumber), BlogResModel::class.java)
        }
        else {
            getLocalModel(CommonParser(), assetManager.openFd("statements_list_doesnt_exist.json"), null, BlogResModel::class.java)
        }
    }

    companion object {
        private const val FAKE_RESPONSE_TIME = 380L
    }
}