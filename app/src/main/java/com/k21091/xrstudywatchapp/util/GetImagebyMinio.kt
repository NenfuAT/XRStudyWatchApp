
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadImageTask(private val listener: OnImageDownloadListener) : AsyncTask<String, Void, Bitmap?>() {

    interface OnImageDownloadListener {
        fun onImageDownloaded(bitmap: Bitmap?)
    }

    override fun doInBackground(vararg params: String?): Bitmap? {
        val imageUrl = params[0]
        var bitmap: Bitmap? = null
        try {
            val inputStream: InputStream = URL(imageUrl).openStream()
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    override fun onPostExecute(result: Bitmap?) {
        super.onPostExecute(result)
        listener.onImageDownloaded(result)
    }
}

