
package com.example.myassignment.utils

enum class CustomResultStatus {
    SUCCESS,
    ERROR
}

data class CustomError(
        val message: String,
        val error: Error? = null,
        val exception: Exception? = null
)

data class CustomResult<out T>(val status: CustomResultStatus, val data: T?, val error: CustomError? = null) {
    companion object {
        fun <T> success(data: T?):CustomResult<T> {
            return CustomResult(CustomResultStatus.SUCCESS, data)
        }

        fun <T> error(msg: String, data: T?, exception: Exception?): CustomResult<T> {
            return CustomResult(CustomResultStatus.ERROR, data, CustomError(msg, null, exception))
        }

        fun <T> error(error: Error?, data: T?, exception: Exception?): CustomResult<T> {
            return CustomResult(CustomResultStatus.ERROR, data, CustomError(error?.localizedMessage
                    ?: "", error, exception))
        }

        fun <T> error(exception: Exception?, data: T?): CustomResult<T> {
            return CustomResult(CustomResultStatus.ERROR, data, CustomError(exception?.localizedMessage
                    ?: "", null, exception))
        }

        fun <T> error(error: CustomError?, data: T?): CustomResult<T> {
            return CustomResult(CustomResultStatus.ERROR, data, error)
        }
    }
}

data class GlobalError(override val message: String): Error()
data class GlobalGoBackError(override val message: String): Error()
data class ForbiddenError(override val message: String): Error()
data class BlogsFetchError(override val message: String): Error()
