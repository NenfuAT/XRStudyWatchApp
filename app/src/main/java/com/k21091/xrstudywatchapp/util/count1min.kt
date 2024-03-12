import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

var elapsedMilliseconds =0f
class Count1Min {
    private var job: Job? = null

    fun startCountDown(listener: (Float) -> Unit) {
        job?.cancel() // Cancel previous job if any
        job = CoroutineScope(Dispatchers.Default).launch {
            val startTime = System.currentTimeMillis()
            val endTime = startTime + 60 * 1000 // 60秒をミリ秒に変換

            while (System.currentTimeMillis() < endTime) {
                val currentTime = System.currentTimeMillis()
                elapsedMilliseconds = (currentTime - startTime).toFloat()
                withContext(Dispatchers.Main) {
                    listener(elapsedMilliseconds)
                }
                //delay(100)
            }
            withContext(Dispatchers.Main) {
                listener(60f * 1000) // Countdown finished, 60秒をミリ秒に変換
            }
        }
    }
}
