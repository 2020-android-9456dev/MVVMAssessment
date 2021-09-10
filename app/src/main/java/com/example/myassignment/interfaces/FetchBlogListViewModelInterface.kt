package com.example.myassignment.interfaces

import androidx.lifecycle.LiveData

interface FetchBlogListViewModelInterface {
    var isLoadingInitial: LiveData<Boolean>
    var isLoadingMore: LiveData<Boolean>
}