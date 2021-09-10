

package com.example.myassignment.network

import androidx.lifecycle.LiveData
import com.example.myassignment.rest.model.blog_model.BlogResModel
import com.example.myassignment.utils.CustomResult

interface BlogslistDataSourceInterface {
    val result : LiveData<CustomResult<BlogResModel>>
    suspend fun getBlogsList(limit: String, lastPageNumber: Int)
}