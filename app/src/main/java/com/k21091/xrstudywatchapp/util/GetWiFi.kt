import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.k21091.xrstudywatchapp.util.OtherFileStorage

class GetWiFi(private val context: Context) {
    private val wifiManager: WifiManager by lazy {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    }

    fun startScanWithInterval(context: Context) {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val handler = Handler(Looper.getMainLooper())

        val SCAN_INTERVAL_MS = 5 * 1000 // 10 seconds

        val scanTask = object : Runnable {
            override fun run() {
                wifiManager.startScan()
                handler.postDelayed(this, SCAN_INTERVAL_MS.toLong())
            }
        }

        // 最初のスキャンを即座に実行する
        scanTask.run()
    }
    fun getScanResult(){
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // パーミッションが許可されていない場合、リクエストを送信してパーミッションを要求
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        } else {
            // パーミッションが許可されている場合、スキャン結果を取得
            performWiFiScan()
        }
    }

    @SuppressLint("MissingPermission")
    private fun performWiFiScan() {
        if (wifiManager.isWifiEnabled) {
            val results = wifiManager.scanResults
            processScanResults(results)
        } else {
            Log.d("GetWiFi", "Wi-Fi is not enabled")
        }
    }

    private fun processScanResults(results: List<ScanResult>) {
        for (result in results) {
            val bssid = result.BSSID
            val level = result.level
            //Log.d("GetWiFi", "Wi-Fi BSSID: $bssid, rssi: $level")
        }
    }
    @SuppressLint("MissingPermission")
    fun getResults(): List<ScanResult> {
        // スキャンを実行して結果を取得する
        performWiFiScan()
        // 最新のスキャン結果を返す
        return wifiManager.scanResults.toList()
    }
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
