package com.k21091.xrstudywatchapp.util

import GetWiFi
import android.content.Context

class CreateCsv(context: Context)  {
    var GetBle= GetBle(context)
    var GetWiFi=GetWiFi(context)
    var OtherFileStorage=OtherFileStorage(context,"${System.currentTimeMillis()}")
    var BleData=mutableListOf<String>()
    var WifiData=mutableListOf<String>()
    fun createcsvdata(count: Int, callback: () -> Unit) {
        GetBle.startScanning { bleResults ->
            bleResults.forEach {
                BleData.add("$count,$it")
            }

            val wifiResults = GetWiFi.getResults()
            for (result in wifiResults) {
                val bssid = result.BSSID
                val level = result.level
                WifiData.add("$count,$bssid,$level,wifi")
            }

            // 処理が完了したらコールバックを呼び出す
            callback()
        }
    }



    fun Savecsv(){
        val Fpdata= mutableListOf<String>()
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