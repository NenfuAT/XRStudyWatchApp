package com.k21091.xrstudywatchapp.util

import android.content.Context
import android.util.Log
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.MonitorNotifier
import org.altbeacon.beacon.RangeNotifier
import org.altbeacon.beacon.Region
import java.util.Timer
import java.util.TimerTask

class GetBle(context: Context) : RangeNotifier, MonitorNotifier {

    private var beaconManager: BeaconManager = BeaconManager.getInstanceForApplication(context)
    private lateinit var mRegion: Region
    private val scannedBeacons: MutableList<String> = mutableListOf()
    private var callback: ((List<String>) -> Unit)? = null

    fun onBeaconServiceConnect() {
        mRegion = Region("iBeacon", null, null, null)
        val IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"
        val ALTBEACON_FORMAT = BeaconParser().setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT)
        val EDDYSTONE_FORMAT = BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_FORMAT))
        beaconManager.beaconParsers.add(ALTBEACON_FORMAT)
        beaconManager.beaconParsers.add(EDDYSTONE_FORMAT)
    }

    fun startScanning(callback: (List<String>) -> Unit) {
        this.callback = callback
        if (!::mRegion.isInitialized) {
            // mRegion が初期化されていない場合、onBeaconServiceConnect() を呼び出して初期化する
            onBeaconServiceConnect()
        }
        beaconManager.addMonitorNotifier(this)
        beaconManager.addRangeNotifier(this)
        beaconManager.startMonitoring(mRegion)
        beaconManager.startRangingBeacons(mRegion)

        // 2秒後にスキャンを停止し、データを返す
        Timer().schedule(object : TimerTask() {
            override fun run() {
                stopScanning()
                callback(scannedBeacons.toList())
                scannedBeacons.clear()
            }
        }, 2000)
    }

    fun stopScanning() {
        beaconManager.stopMonitoring(mRegion)
        beaconManager.stopRangingBeacons(mRegion)
    }

    fun onDestroy() {
        beaconManager.stopMonitoring(mRegion)
        beaconManager.stopRangingBeacons(mRegion)
    }

    override fun didEnterRegion(region: Region?) {
        //Log.d("iBeacon", "Enter Region ${region?.uniqueId}")
    }

    override fun didExitRegion(region: Region?) {
        //Log.d("iBeacon", "Exit Region ${region?.uniqueId}")
    }

    override fun didRangeBeaconsInRegion(beacons: MutableCollection<Beacon>?, region: Region?) {
        Log.d("BleCentral", "Detected Beacons:")
        beacons?.forEach { beacon ->
            val beaconString = "${beacon.id1},${beacon.rssi},ble"
            //Log.d("BleCentral", beaconString)
            scannedBeacons.add(beaconString) // スキャンしたビーコンをリストに追加
        }
    }

    override fun didDetermineStateForRegion(state: Int, region: Region?) {
        Log.d("BleCentral", "Determine State: $state")
    }
}
