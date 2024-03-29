package com.k21091.xrstudywatchapp.service

import GetBLE
import GetWiFi
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import com.k21091.xrstudywatchapp.util.BleData
import com.k21091.xrstudywatchapp.util.GetLocation
import com.k21091.xrstudywatchapp.util.WifiData
import com.k21091.xrstudywatchapp.util.buildMultipartFormDataBody
import com.k21091.xrstudywatchapp.util.scanCsvName
import com.k21091.xrstudywatchapp.util.scanCsvPath
import com.k21091.xrstudywatchapp.util.sendRequest
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SpotScanService : Service() {
    private var thread: Thread? = null
    private var isServiceRunning = false
    private val TAG = "SpotScanService"
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("spot", "start")
        val CreateCsv = CreateCsv(this, 5)
        isServiceRunning = true
        // サービスが開始されたときの処理
        thread = Thread(Runnable {
            val formMap = mutableMapOf<String, String>().apply {
                put("latitude", "35.681236")
                put("longitude", "-139.767125")
            }
            val fileList: MutableList<Pair<String, String>> = mutableListOf()

            // バックグラウンドスレッド内でループ処理を行う
            // CreateCsv.createcsvdata内の処理をループさせる
            while (isServiceRunning) {
                var isRequestCompleted = false
                CreateCsv.createcsvdata {
                    var GetLocation = GetLocation(this)
                    var locations = GetLocation.getLatitudeAndLongitudeAsString()
                    formMap["latitude"] = locations?.get("latitude") ?: ""
                    formMap["longitude"] = locations?.get("longitude") ?: ""
                    fileList.add(Pair("rawDataFile", scanCsvPath.value))
                    val body = buildMultipartFormDataBody(formMap, fileList)

                    if (isServiceRunning) {
                        sendRequest(body,"api/object/around/get",
                            onResponse = { response ->
                                // レスポンスが正常に受信された場合の処理
                                println("Response received: $response")
                                // 必要な処理を実行
                                isRequestCompleted = true
                            }
                        ) { errorMessage ->
                            // エラーが発生した場合の処理
                            println("Request failed with exception: $errorMessage")
                            // エラー処理を実行
                            isRequestCompleted = true
                        }
                    }
                }

                // リクエストが完了するのを待つ
                while (!isRequestCompleted) {
                }
            }

        })

        thread?.start()

        // サービスが停止しても自動的に再起動しないようにする場合は、START_NOT_STICKY を返します
        return START_NOT_STICKY
    }


    override fun onDestroy() {
        // サービスが破棄されたときの処理
        thread?.interrupt() // スレッドを終了させます
        isServiceRunning = false
        Log.d("spot", "end")
        super.onDestroy()
    }

}

class CreateCsv(context: Context, getCount: Int) {
    val context: Context = context
    var getCount = getCount
    var GetBle = GetBLE()
    var GetWiFi = GetWiFi(context)
    var OtherFileStorage = OtherFileStorage(context, "${System.currentTimeMillis()}")

    var count = 0

    fun createcsvdata(completion: () -> Unit) {
        if (count < getCount) {
            GetBle.startScan(count) { bleResults ->
                BleData.addAll(bleResults)
                val wifiResults = GetWiFi.getResults()
                for (result in wifiResults) {
                    val bssid = result.BSSID
                    val level = result.level
                    WifiData.add("$count,$level,$bssid")
                }
                count++
                createcsvdata(completion) // 再帰的に次のスキャンを実行
            }
        } else {
            Savecsv()
            count = 0
            completion() // 5回のスキャンが終了したらコールバックを呼び出す
        }
    }


    fun Savecsv() {
        val Fpdata = mutableListOf<String>()
        for (result in BleData) {
            Fpdata.add(result)
        }
        for (result in WifiData) {
            Fpdata.add(result)
        }
        Fpdata.sortBy { it.split(",")[0].toInt() }
        for (result in Fpdata) {
            OtherFileStorage.writeText(result)
        }
        BleData.clear()
        WifiData.clear()
    }
}

class OtherFileStorage(val context: Context, filename: String) {
    // コンストラクタで直接Contextを受け取る
    val fileAppend: Boolean = true // true=追記, false=上書き
    var fileName: String = filename
    val extension: String = ".csv"
    val FilePath =
        context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString()
            .plus("/").plus(fileName).plus(extension) //内部ストレージのDocumentのURL

    init {
        writeText("gets,rssi,address")
    }

    fun writeText(text: String) {
        scanCsvName.value = fileName.plus(extension)
        scanCsvPath.value = FilePath
        val fil = FileWriter(FilePath, fileAppend)
        val pw = PrintWriter(BufferedWriter(fil))
        pw.println(text)
        pw.close()
    }
}
