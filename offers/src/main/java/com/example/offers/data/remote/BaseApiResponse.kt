package com.example.offers.data.remote

import com.example.core_data.models.NetworkResult
import retrofit2.Response

abstract class BaseApiResponse {
    suspend fun <T> safeApiCall(api: suspend () -> Response<T>): NetworkResult<T> {
        try {
            val response = api()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResult.Success(
                        data = body
                    )
                } ?: return errorMessage(EMPTY_BODY)
            } else {
                return errorMessage("${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            return errorMessage(e.message.toString())
        }
    }

    private fun <T> errorMessage(e: String): NetworkResult.Error<T> = NetworkResult.Error(
        message = ERROR + e,
        data = null
    )

    companion object {
        private const val ERROR = "API call failed with error:"
        private const val EMPTY_BODY = "body is empty"
    }
}