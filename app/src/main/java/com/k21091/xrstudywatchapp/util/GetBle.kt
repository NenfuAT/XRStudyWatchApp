import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log

class GetBLE{
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val bluetoothLeScanner: BluetoothLeScanner? = bluetoothAdapter?.bluetoothLeScanner
    private val handler = Handler()

    private val SCAN_PERIOD: Long = 5000
    private var results = mutableListOf<String>()
    private var scanCallback: ScanCallback? = null

    fun startScan(count:Int,callback: (List<String>) -> Unit) {
        bluetoothLeScanner?.let { scanner ->
            if (scanCallback == null) {
                results.clear() // スキャンが開始される前に結果をクリア
                scanCallback = object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        super.onScanResult(callbackType, result)
                        val uuids = result.scanRecord?.serviceUuids
                        val receiveRssi = result.rssi
                        uuids?.forEach { uuid ->
                            val uuidString = uuid.uuid.toString()
                            results.add("$count,$uuidString,$receiveRssi")
                        }
                    }

                    override fun onScanFailed(errorCode: Int) {
                        super.onScanFailed(errorCode)
                        Log.e("GetBLE", "Scan failed with error code $errorCode")
                    }
                }
                scanner.startScan(scanCallback)
                handler.postDelayed({
                    stopScan()
                    callback(results) // スキャンが終了した後にコールバックで結果を返す
                }, SCAN_PERIOD)
                Log.d("GetBLE", "Batch scan started")
            } else {
                Log.d("GetBLE", "Scan already running")
            }
        }
    }

    private fun stopScan() {
        scanCallback?.let { bluetoothLeScanner?.stopScan(it) }
        scanCallback = null
        Log.d("GetBLE", "Batch scan stopped")
    }
}

