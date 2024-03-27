package com.k21091.xrstudywatchapp.util

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okio.Buffer
import okio.IOException
import java.io.File
import java.util.concurrent.TimeUnit

class SendHttp {
    fun buildMultipartFormDataBody(formData: Map<String, String>, fileParams: List<Pair<String, String>>): RequestBody {
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

    suspend fun sendRequest(body: RequestBody) {
        return withContext(Dispatchers.IO) {
            val client = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS) // 読み取りタイムアウト時間を30秒に設定
                .writeTimeout(30, TimeUnit.SECONDS) // 書き込みタイムアウト時間を30秒に設定
                .connectTimeout(30, TimeUnit.SECONDS) // 接続タイムアウト時間を30秒に設定
                .build() // OkHttpClientを構築

            val request = Request.Builder()
                //.url("http://10.0.2.2:8084/api/object/create")
                .url("http://192.168.11.21:8084/api/object/create")
                .post(body)
                .build()

            // enqueue()メソッドを使用して非同期リクエストを実行し、レスポンスを受け取る
            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    // リクエストの失敗時の処理
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBodyString = response.body?.string()
                    Log.d("res","$responseBodyString")
                }
            })
        }
    }


    fun requestBodyToString(requestBody: RequestBody): String {
        val buffer = Buffer()
        requestBody.writeTo(buffer)
        return buffer.readUtf8()
    }


}