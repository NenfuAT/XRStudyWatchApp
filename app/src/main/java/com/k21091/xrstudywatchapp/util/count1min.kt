import com.k21091.xrstudywatchapp.util.CreateCsv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var elapsedMilliseconds =0f
private var job: Job? = null
class Count1Min {


    fun startCountDown(listener: (Float) -> Unit) {
        job?.cancel() // Cancel previous job if any
        job = CoroutineScope(Dispatchers.Default).launch {
            val startTime = System.currentTimeMillis()
            val endTime = startTime + 60 * 1000 // 60秒をミリ秒に変換
            var currentTime = startTime

            while (currentTime < endTime && isActive) { // isActive を追加してチェック
                currentTime = System.currentTimeMillis()
                val elapsedMilliseconds = (currentTime - startTime).toFloat()
                withContext(Dispatchers.Main) {
                    listener(elapsedMilliseconds)
                }
                delay(100) // 100ミリ秒待機
            }
            if (isActive) {
                withContext(Dispatchers.Main) {
                    listener(60f * 1000) // Countdown finished, 60秒をミリ秒に変換
                }
            }
        }
    }

}
fun stopCountDown() {
    job?.cancel()
    elapsedMilliseconds = 0f
}