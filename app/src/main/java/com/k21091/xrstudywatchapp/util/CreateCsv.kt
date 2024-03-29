package com.k21091.xrstudywatchapp.util

import GetBLE
import GetWiFi
import android.content.Context
import kotlinx.coroutines.*

var BleData = mutableListOf<String>()
var WifiData = mutableListOf<String>()
var stop: Boolean = false
class CreateCsv(context: Context,getCount: Int) {
    var getCount=getCount
    var GetBle = GetBLE()
    var GetWiFi = GetWiFi(context)
    var OtherFileStorage = OtherFileStorage(context, "${System.currentTimeMillis()}")

    var count = 0

    fun createcsvdata(completion: () -> Unit) {
        if (count < getCount) {
            GetBle.startScan(count){bleResults->
                if (stop){
                    count=getCount
                    completion()

                }
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
            completion()
            count=0
        // 5回のスキャンが終了したらコールバックを呼び出す
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
fun cancelScan() {
    stop=true
    BleData.clear()
    WifiData.clear()
}