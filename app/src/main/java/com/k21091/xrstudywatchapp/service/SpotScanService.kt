package com.k21091.xrstudywatchapp.service

import android.app.Service

import android.content.Intent

import android.os.IBinder
import android.util.Log
import com.k21091.xrstudywatchapp.util.AroundObject
import com.k21091.xrstudywatchapp.util.ArrivingObject
import com.k21091.xrstudywatchapp.util.CreateCsv
import com.k21091.xrstudywatchapp.util.Laboratory
import com.k21091.xrstudywatchapp.util.University
import com.k21091.xrstudywatchapp.util.areaObject
import com.k21091.xrstudywatchapp.util.buildMultipartFormDataBody
import com.k21091.xrstudywatchapp.util.cancelScan
import com.k21091.xrstudywatchapp.util.csvFilePath
import com.k21091.xrstudywatchapp.util.isServiceRunning
import com.k21091.xrstudywatchapp.util.sendRequest
import com.k21091.xrstudywatchapp.util.spotObject
import com.k21091.xrstudywatchapp.util.userId
import org.json.JSONObject


class SpotScanService : Service() {

    private var thread: Thread? = null

    private val TAG = "SpotScanService"
    private lateinit var CreateCsv: CreateCsv
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service created")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("spot", "start")
        isServiceRunning.value = true
        CreateCsv = CreateCsv(this, 5,"scan")
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
                        formMap["latitude"] = latitude
                        formMap["longitude"] = longitude
                        println(latitude)
                        println(longitude)
                        fileList.add(Pair("rawDataFile", csvFilePath.value))
                        val body = buildMultipartFormDataBody(formMap, fileList)
                        sendRequest(body, userId.value, "api/object/around/get",
                            onResponse = { response ->
                                Log.d("Response received","$response")
                                val jsonObject = JSONObject(response)
                                val arrivingObjectsArray = jsonObject.getJSONArray("arrivingObjects")
                                val aroundObjectsArray = jsonObject.getJSONArray("aroundObjects")
                                // JSONから取得したIDを保持するセットを作成します
                                val arrivingObjectIds = mutableSetOf<String>()
                                val aroundObjectIds = mutableSetOf<String>()
                                val arrivingmap = spotObject.value.toMutableMap()
                                val aroundmap = areaObject.value.toMutableMap()
                                for (i in 0 until arrivingObjectsArray.length()) {
                                    val obj = arrivingObjectsArray.getJSONObject(i)
                                    val arrivingObject = ArrivingObject(
                                        obj.getString("id"),
                                        obj.getString("width"),
                                        obj.getString("height"),
                                        obj.getString("size"),
                                        obj.getString("viewUrl")
                                    )
                                    arrivingmap[arrivingObject.id]=arrivingObject
                                    arrivingObjectIds.add(arrivingObject.id)
                                }

                                // aroundObjectsをパースしてリストに追加
                                for (i in 0 until aroundObjectsArray.length()) {
                                    val obj = aroundObjectsArray.getJSONObject(i)
                                    val laboratoryObj = obj.getJSONObject("laboratory")
                                    val universityObj = obj.getJSONObject("university")
                                    val laboratory = Laboratory(
                                        laboratoryObj.getString("name"),
                                        laboratoryObj.getString("location"),
                                        laboratoryObj.getString("roomNum")
                                    )
                                    val university = University(
                                        universityObj.getString("name"),
                                        universityObj.getString("undergraduate"),
                                        universityObj.getString("department"),
                                        universityObj.getString("major")
                                    )
                                    val aroundObject = AroundObject(
                                        obj.getString("id"),
                                        laboratory,
                                        university
                                    )
                                    aroundmap[aroundObject.id] = aroundObject
                                    aroundObjectIds.add(aroundObject.id)
                                }

                                // リストから存在しないIDに対応するオブジェクトを削除
                                arrivingmap.keys.removeAll { it !in arrivingObjectIds }
                                aroundmap.keys.removeAll { it !in aroundObjectIds }
                                areaObject.value=aroundmap
                                spotObject.value=arrivingmap
                                //File(csvFilePath.value).delete()
                                fileList.clear()
                                isRequestCompleted = true


                            }
                        ) { errorMessage ->
                            Log.d("Response received", errorMessage)
                            //File(csvFilePath.value).delete()
                            isRequestCompleted = true
                        }
                    }
                    else{
                        Log.d("isCompleted","false")
                        //File(csvFilePath.value).delete()
                        isRequestCompleted=true
                    }
                }
                println("rcomp:$isRequestCompleted")

                try {
                    Thread.sleep(18000)
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




