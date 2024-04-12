package com.k21091.xrstudywatchapp.util

import GetBLE
import GetWiFi
import android.content.Context
import android.util.Log
import kotlinx.coroutines.*

var BleData = mutableListOf<String>()
var WifiData = mutableListOf<String>()
// stopフラグを定義する
private var stopScanning = false
class CreateCsv(var context: Context,getCount: Int,val type:String) {
    var getCount=getCount
    var GetBle = GetBLE()
    var GetWiFi = GetWiFi(context)

    var count = 0

    lateinit var  OtherFileStorage:OtherFileStorage
    // スキャンを実行する関数を修正する
    fun createcsvdata(completion: (Boolean) -> Unit) {
        if (count==0){
            OtherFileStorage = OtherFileStorage(context, "${System.currentTimeMillis()}${type}",type)
        }
        if (count < getCount) {
            if (stopScanning) {
                stopScanning = false
                count=0
                // スキャンがキャンセルされた場合はコールバックを返さない
                completion(false)
                return
            }
            GetBle.startScan(count) { bleResults ->

                    BleData.addAll(bleResults)
                    val wifiResults = GetWiFi.getResults()
                    for (result in wifiResults) {
                        val bssid = result.BSSID
                        val level = result.level
                        WifiData.add("$count,$level,$bssid,wifi")
                    }
                    count++
                    createcsvdata(completion) // 再帰的に次のスキャンを実行
                }

        } else {
            Savecsv()
            completion(true)
            count = 0
            stopScanning = false // スキャンが完了した後、stopフラグをリセットする
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
        Fpdata.add(0,"gets,rssi,address,type")
        for (result in Fpdata) {
            OtherFileStorage.writeText(result)
        }

        BleData.clear()
        WifiData.clear()
    }
}
fun cancelScan() {
    Log.d("cancelScan","cancelS")
    stopScanning = true
    BleData.clear()
    WifiData.clear()
}