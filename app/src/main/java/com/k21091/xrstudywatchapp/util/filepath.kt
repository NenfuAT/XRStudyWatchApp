package com.k21091.xrstudywatchapp.util

import android.net.Uri
import androidx.compose.runtime.mutableStateOf

var imageFilePath = mutableStateOf<String>("")
var imageFileName = mutableStateOf<String>("")
var csvFilePath = mutableStateOf<String>("")
var csvFileName = mutableStateOf<String>("")

var userId = mutableStateOf<String>("")
var isServiceRunning = mutableStateOf<Boolean>(false)

//スポット
var spotObject = mutableListOf<Map<String,String>>()
//近く
var areaObject = mutableListOf<Map<String,String>>()