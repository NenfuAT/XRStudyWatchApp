package com.k21091.xrstudywatchapp.util

import android.content.Context
import android.os.Environment
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.PrintWriter

class OtherFileStorage(context: Context, filename: String) {

    val fileAppend : Boolean = true //true=追記, false=上書き
    val context:Context = context
    var fileName : String = filename
    val extension : String = ".csv"
    val filePath: String = context.applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).toString().plus("/").plus(fileName).plus(extension) //内部ストレージのDocumentのURL
    init {
        writeText("gets,rssi,address")
    }
    fun writeText(text:String){
        val fil = FileWriter(filePath,fileAppend)
        val pw = PrintWriter(BufferedWriter(fil))
        pw.println(text)
        pw.close()
    }
}