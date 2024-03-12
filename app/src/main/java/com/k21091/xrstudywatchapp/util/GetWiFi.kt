import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.app.ActivityCompat
import com.k21091.xrstudywatchapp.util.OtherFileStorage

class GetWiFi(private val context: Context) {
    private val wifiManager: WifiManager by lazy {
        context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
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
        val OtherFileStorage= OtherFileStorage(context,"wifi")
        for (result in results) {
            val bssid = result.BSSID
            val level = result.level
            //Log.d("GetWiFi", "Wi-Fi BSSID: $bssid, rssi: $level")
        }
    }
    @SuppressLint("MissingPermission")
    fun getResults(): List<ScanResult> {
        return wifiManager.scanResults.toList()
    }
    companion object {
        private const val PERMISSION_REQUEST_CODE = 100
    }
}
