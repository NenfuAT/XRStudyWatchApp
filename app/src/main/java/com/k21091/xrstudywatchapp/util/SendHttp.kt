package com.k21091.xrstudywatchapp.util

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.IOException
import java.io.File
import java.util.concurrent.TimeUnit

fun buildMultipartFormDataBody(
    formData: Map<String, String>,
    fileParams: List<Pair<String, String>>
): RequestBody {
    val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)

    // フォームデータを追加
    formData.forEach { (key, value) ->
        requestBodyBuilder.addFormDataPart(key, value)
    }

    // ファイルを追加
    fileParams.forEachIndexed { _, (fileKey, filePath) ->
        val file = File(filePath)
        requestBodyBuilder.addFormDataPart(
            fileKey,
            file.name,
            file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        )
    }

    return requestBodyBuilder.build()
}

fun buildJsonDataBody(formData: Map<String, String>): RequestBody {
    val jsonString = Json.encodeToString(formData)
    return jsonString.toRequestBody("application/json".toMediaType())
}

fun sendRequest(body: RequestBody,api:String, onResponse: (String?) -> Unit, onFailure: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("http://10.0.2.2:8084/$api")
            .post(body)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBodyString = response.body?.string()
            // レスポンスをコールバックに渡す
            onResponse(responseBodyString)
        } catch (e: IOException) {
            // エラーが発生した場合、エラーメッセージをコールバックに渡す
            onFailure(e.message ?: "Unknown error")
        }
    }
}

fun requestBodyToString(requestBody: RequestBody): String {
    val buffer = Buffer()
    requestBody.writeTo(buffer)
    return buffer.readUtf8()
}
