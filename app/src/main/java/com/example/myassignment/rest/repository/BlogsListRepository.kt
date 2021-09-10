

package com.example.myassignment.rest.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myassignment.network.BaseRepository
import com.example.myassignment.network.BlogslistDataSourceInterface
import com.example.myassignment.network.RestAPIErrors
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.utils.CustomError
import com.example.myassignment.utils.CustomResult
import com.example.myassignment.utils.CustomResultStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

interface BlogsListRepositoryInterface {
    suspend fun getBlogsList(limit: String, lastPageNumber: Int?): LiveData<out BlogResModel>

    // Defaults
    val isLoading: LiveData<Boolean>
    val displayError: MutableLiveData<CustomError>

    val isLoadingInitial: LiveData<Boolean>
    val isLoadingMore: LiveData<Boolean>
}

class BlogsListRepository(private val blogsDataSource: BlogslistDataSourceInterface? = null) : BlogsListRepositoryInterface, BaseRepository() {

    private val _isLoadingInitial = MutableLiveData<Boolean>()

    override var isLoadingInitial: LiveData<Boolean> = _isLoadingInitial

    private val _isLoadingMore = MutableLiveData<Boolean>()
    override var isLoadingMore: LiveData<Boolean> = _isLoadingMore

    // Dao
    var currentPage: Int = 1
    // Dao
    private val currentDao : MutableLiveData<BlogResModel> = MutableLiveData()

    var hasMorePages: Boolean = true

    init {
        blogsDataSource?.apply {
            if (!result.hasObservers()) {
                result.observeForever { newData ->
                    handleResult(newData)
                }
            }
        }
    }

    private fun handleResult(newData: CustomResult<BlogResModel>) {
        GlobalScope.launch(Dispatchers.IO) {

            if (newData.status == CustomResultStatus.ERROR) {
                displayError.postValue(RestAPIErrors.GlobalConnectionError.coopError)
            } else {
                newData.data?.let {
                    println("Final Response : "+it.size)
                    currentDao.postValue(it)
                    hasMorePages = true
                }
            }

            if (isLoadingInitial.value == true) {
                _isLoadingInitial.postValue(false)
            }
            if (isLoadingMore.value == true) {
                _isLoadingMore.postValue(false)
            }
        }
    }

    private suspend fun initBlogsData(limit: String, page: Int) {
        fetchBlogs(limit, page)
    }

    private suspend fun fetchBlogs(limit: String, page: Int) {
        if (page == 1) {
            _isLoadingInitial.postValue(true)
        } else {
            _isLoadingMore.postValue(true)
        }
        blogsDataSource?.getBlogsList(limit, page)
    }

    override suspend fun getBlogsList(limit: String, lastPageNumber: Int?): LiveData<out BlogResModel> {
        currentPage = lastPageNumber ?: 0
        initBlogsData(limit, currentPage)
        return withContext(Dispatchers.IO) {
            return@withContext currentDao
        }
    }

}

