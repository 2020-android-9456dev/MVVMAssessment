

package com.example.myassignment.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myassignment.utils.CustomError
import com.example.myassignment.utils.CustomResult
import java.io.IOException

open class BaseRepository {

    // Properties
    val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Errors
    val displayError = MutableLiveData<CustomError>()

    suspend fun <T: Any> safeApiCall(call : suspend() -> RestApiResponse<T>, errorMessage : String) : CustomResult<T> {
        return safeApiResult(call, errorMessage)
    }

    private suspend fun<T : Any> safeApiResult(call: suspend() -> RestApiResponse<T>, errorMessage: String)
            : CustomResult<T> {
        val response = call.invoke()

        if (response.isSuccessful()) {
            return if (response.data != null) {
                CustomResult.success(response.data)
            } else {
                CustomResult.error(RestAPIErrors.NoDataException.coopError, null)
            }
        }

        // TODO handle errors properly
        return CustomResult.error(IOException("Error Occurred during getting safe Api result, Custom ERROR " +
                "- $errorMessage"), null)
    }
}