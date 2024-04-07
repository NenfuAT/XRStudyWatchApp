package com.k21091.xrstudywatchapp.util

import android.util.Log
import com.k21091.xrstudywatchapp.server
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
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
import java.util.Base64
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
        val mediaType = guessMediaType(file.name)
        println(mediaType)
        requestBodyBuilder.addFormDataPart(
            fileKey,
            file.name,
            file.asRequestBody(mediaType)
        )
    }
    return requestBodyBuilder.build()
}

inline fun <reified T: Any> buildJsonDataBody(data: T): RequestBody {
    val jsonString = Json.encodeToString(data)
    return jsonString.toRequestBody("application/json".toMediaType())
}

fun sendRequest(body: RequestBody,header: String,api:String, onResponse: (String?) -> Unit, onFailure: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
        lateinit var request : Request
        if (header==""){
                request=Request.Builder()
                .url("$server$api")
                .post(body)
                .build()
        }
        else{
            val basic= encodeBasic(header,"")
            request=Request.Builder()
            .url("$server$api")
            .addHeader("Authorization",basic)
            .post(body)
            .build()
        }

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

fun encodeBasic(username: String, password: String): String {
    val credentials = "$username:$password"
    return Base64.getEncoder().encodeToString(credentials.toByteArray())
}

fun guessMediaType(fileName: String): MediaType? {
    val extension = fileName.substringAfterLast(".", "")
    return when (extension.toLowerCase()) {
        "png" -> "image/png".toMediaTypeOrNull()
        "jpg", "jpeg" -> "image/jpeg".toMediaTypeOrNull()
        "pdf" -> "application/pdf".toMediaTypeOrNull()
        "csv" -> "text/csv".toMediaTypeOrNull()
        else -> "application/octet-stream".toMediaTypeOrNull()
    }
}