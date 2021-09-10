
package com.example.myassignment.interfaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myassignment.utils.CustomError


interface FetchDataViewModelInterface {
    // Binding
    var isLoading: LiveData<Boolean>
    val displayError: MutableLiveData<CustomError>
}
