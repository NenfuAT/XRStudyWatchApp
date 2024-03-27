package com.k21091.xrstudywatchapp.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager

class GetLocation(private val context: Context) {
    @SuppressLint("MissingPermission")
    fun getLatitudeAndLongitudeAsString(): Map<String, String>? {
        // LocationManager を取得
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        // GPS_PROVIDER を使用して最後の既知の位置情報を取得
        val lastLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        // 最後の位置情報が存在すれば緯度と経度のマップを返す
        return lastLocation?.let { mapOf("latitude" to String.format("%.6f", it.latitude), "longitude" to String.format("%.6f", it.longitude)) }
    }
}