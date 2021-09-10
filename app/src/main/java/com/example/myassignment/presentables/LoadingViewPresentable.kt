

package com.example.myassignment.presentables

import androidx.fragment.app.Fragment
import com.example.myassignment.commonview.CustomProgressBar

enum class LoadingViewStatus {
    VISIBLE, HIDDEN
}

interface LoadingViewPresentable {

    val progressBar: CustomProgressBar?

    fun Fragment.setLoadingViewStatus(status: LoadingViewStatus) {
        defaultLoadingViewStatus(status)
    }

    fun Fragment.defaultLoadingViewStatus(status: LoadingViewStatus) {
        when (status) {
            LoadingViewStatus.VISIBLE -> {
                progressBar?.showProgressBar()
            }
            LoadingViewStatus.HIDDEN -> {
                progressBar?.hideProgressBar()
            }
        }
    }
}



