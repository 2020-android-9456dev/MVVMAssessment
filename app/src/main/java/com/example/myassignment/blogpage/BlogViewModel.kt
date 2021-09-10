package com.example.myassignment.blogpage

import android.content.Context
import androidx.lifecycle.*
import com.example.myassignment.interfaces.FetchBlogListViewModelInterface
import com.example.myassignment.interfaces.FetchDataViewModelInterface
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.rest.repository.BlogsListRepository
import com.example.myassignment.utils.CustomError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BlogViewModelFactory(private val context: Context, private val blogListRepository: BlogsListRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BlogViewModel::class.java)) {
            return BlogViewModel(context, blogListRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class BlogViewModel(private val context: Context, private val blogListRepository: BlogsListRepository) : ViewModel(), FetchDataViewModelInterface,
    FetchBlogListViewModelInterface {

    override var isLoadingInitial: LiveData<Boolean> = blogListRepository.isLoadingInitial
    override var isLoadingMore: LiveData<Boolean> = blogListRepository.isLoadingMore
    override var isLoading: LiveData<Boolean> = blogListRepository.isLoading
    override var displayError: MutableLiveData<CustomError> = blogListRepository.displayError

    private var _blogListData: MutableLiveData<BlogResModel> = MutableLiveData()
    var blogListData: MutableLiveData<BlogResModel> = _blogListData

    private var _blogListMoreData: MutableLiveData<BlogResModel> = MutableLiveData()
    var blogListMoreData: MutableLiveData<BlogResModel> = _blogListMoreData

    var pageNumber : Int = 1

    fun fetchBlogData() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = blogListRepository.getBlogsList("10", pageNumber)

            withContext(Dispatchers.Main) {
                if (!response.hasObservers()) {
                    response.observeForever {
                        _blogListData.postValue(it)
                    }
                }
            }
        }
    }

    fun fetchMoreBlogData() {
        viewModelScope.launch(Dispatchers.IO) {
            pageNumber = pageNumber+1
            val response = blogListRepository.getBlogsList("10", pageNumber)

            withContext(Dispatchers.Main) {
                if (!response.hasObservers()) {
                    response.observeForever {
                        _blogListMoreData.postValue(it)
                    }
                }
            }
        }
    }
}