package com.k21091.xrstudywatchapp.service

import GetBLE
import GetWiFi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.k21091.xrstudywatchapp.util.CreateCsv
import com.k21091.xrstudywatchapp.util.GetLocation
import com.k21091.xrstudywatchapp.util.buildMultipartFormDataBody
import com.k21091.xrstudywatchapp.util.cancelScan
import com.k21091.xrstudywatchapp.util.csvFilePath
import com.k21091.xrstudywatchapp.util.isServiceRunning
import com.k21091.xrstudywatchapp.util.sendRequest
import com.k21091.xrstudywatchapp.util.userId
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SpotScanService : Service() {
    private var thread: Thread? = null

    private val TAG = "SpotScanService"
    private lateinit var CreateCsv: CreateCsv
    override fun onCreate() {
        super.onCreate()
        CreateCsv = CreateCsv(this, 5)
        Log.d(TAG, "Service created")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("spot", "start")
        isServiceRunning.value = true

        // サービスの処理を関数に切り出す
        performServiceTask()

        return START_STICKY
    }


    override fun onDestroy() {
        // サービスが破棄されたときの処理
        cancelScan()
        isServiceRunning.value = false
        thread?.interrupt()
        Log.d("spot", "end")
        super.onDestroy()
    }

    private fun performServiceTask() {
        thread = Thread(Runnable {
            val formMap = mutableMapOf<String, String>().apply {
                put("latitude", "35.681236")
                put("longitude", "-139.767125")
            }
            val fileList: MutableList<Pair<String, String>> = mutableListOf()
            var isRequestCompleted = false
            while (isServiceRunning.value) {
                Log.d("hoge","loop")

                CreateCsv.createcsvdata {isCompleted ->
                    if (isCompleted) {
                        Log.d("isCompleted","true")
                        var GetLocation = GetLocation(this)
                        var locations = GetLocation.getLatitudeAndLongitudeAsString()
                        formMap["latitude"] = locations?.get("latitude") ?: ""
                        formMap["longitude"] = locations?.get("longitude") ?: ""
                        fileList.add(Pair("rawDataFile", csvFilePath.value))
                        val body = buildMultipartFormDataBody(formMap, fileList)
                        sendRequest(body, userId.value, "api/object/around/get",
                            onResponse = { response ->
                                Log.d("Response received","$response")
                                isRequestCompleted = true
                                File(csvFilePath.value).delete()

                            }
                        ) { errorMessage ->
                            Log.d("Response received", errorMessage)
                            File(csvFilePath.value).delete()
                            isRequestCompleted = true
                        }
                    }
                    else{
                        Log.d("isCompleted","false")
                        File(csvFilePath.value).delete()
                        isRequestCompleted=true
                    }
                }
                println("rcomp:$isRequestCompleted")

                try {
                    Thread.sleep(20000)
                } catch (e: InterruptedException) {
                    Log.d(TAG, "Thread interrupted, stopping service task")
                    break
                }
                println("rcomp:$isRequestCompleted")
                isRequestCompleted = false
                println(isServiceRunning)
            }
            Log.d("loop","outloop")
        })

        thread?.start()
    }



}




