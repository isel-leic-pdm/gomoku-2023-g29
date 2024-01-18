package ipl.isel.daw.gomoku.utils

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import ipl.isel.daw.gomoku.utils.hypermedia.ApplicationJsonType
import ipl.isel.daw.gomoku.utils.hypermedia.OrdinaryJsonType
import ipl.isel.daw.gomoku.utils.hypermedia.SirenMediaType
import okhttp3.OkHttpClient
import okhttp3.Response

/*
 * Extension function used to send [this] request using [okHttpClient] and process the
 * received response with the given [handler]. Note that [handler] is called from a
 * OkHttp IO Thread.
 *
 * @receiver the request to be sent
 * @param okHttpClient  the client from where the request is sent
 * @param handler       the handler function, which is called from a IO thread.
 *                      Be mindful of threading issues.
 * @return the result of the response [handler]
 * @throws  [IOException] if a communication error occurs.
 * @throws  [Throwable] if any error is thrown by the response handler.

suspend fun <T> Request.send(okHttpClient: OkHttpClient, handler: (Response) -> T): T =

    suspendCoroutine { continuation ->
        okHttpClient.newCall(request = this).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                continuation.resumeWithException(e)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    continuation.resume(handler(response))
                } catch (t: Throwable) {
                    continuation.resumeWithException(t)
                }
            }
        })
    }*/


fun getClient(httpClient: OkHttpClient, bearer: String) = httpClient.newBuilder()
    .addInterceptor { chain ->
        val originalRequest = chain.request()

        val builder = originalRequest.newBuilder()
            .header("Authorization", "Bearer $bearer")

        val newRequest = builder.build()
        chain.proceed(newRequest)
    }.build()


inline fun <reified T : Any> handleResponse(response: Response, jsonEncoder: Gson): T {
    val contentType = response.body?.contentType()
    val body = response.body?.string()
    return if (response.isSuccessful && response.body != null &&
        (contentType == ApplicationJsonType || contentType == SirenMediaType || contentType == OrdinaryJsonType)
    ) {
        try {
            jsonEncoder.fromJson(body, T::class.java)
        } catch (e: JsonSyntaxException) {
            throw UnexpectedResponseException(response)
        }
    } else throw ResponseException(body.orEmpty())
}

abstract class ApiException(msg: String) : Exception(msg)

/**
 * Exception throw when an unexpected response was received from the API.
 */
class UnexpectedResponseException(
    response: Response
) : ApiException("Unexpected ${response.code} response from the API.")

class ResponseException(
    response: String
) : ApiException(response)