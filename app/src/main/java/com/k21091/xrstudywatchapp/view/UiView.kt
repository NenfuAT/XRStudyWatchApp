package com.k21091.xrstudywatchapp.view

import Count1Min
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k21091.xrstudywatchapp.MainActivity
import com.k21091.xrstudywatchapp.service.SpotScanService

import com.k21091.xrstudywatchapp.util.CreateCsv
import com.k21091.xrstudywatchapp.util.cancelScan
import com.k21091.xrstudywatchapp.util.searchobject
import com.k21091.xrstudywatchapp.util.spotObject
import stopCountDown


val loginButtonChecked = mutableStateOf(false)
val createUserButtonChecked = mutableStateOf(false)
val termsButtonChecked = mutableStateOf(false)

@Composable
fun LoginView(
    toMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = Color.White
            )
    )
    {
        if (
            !loginButtonChecked.value &&
            !createUserButtonChecked.value &&
            !termsButtonChecked.value
        ) {

            LoginHome(modifier.align(Alignment.Center))
        }

        if (loginButtonChecked.value) {
            LoginMenu(
                toMain,
                modifier.align(Alignment.Center)
            )
        }

        if (createUserButtonChecked.value) {
            CreateUserMenu(modifier.align(Alignment.Center))
        }

    }
}

class UiView(private val context: Context, getContent: ActivityResultLauncher<String>) {
    private val Buttons = ButtonParts(this)
    private val Menus = MenuParts(this, getContent)
    val uploadButtonChecked = mutableStateOf(false)
    val nearObjectButtonChecked = mutableStateOf(false)
    val serviceIntent = Intent(context, SpotScanService::class.java)

    init {
        if (!isServiceRunning(SpotScanService::class.java)) {
            context.startService(serviceIntent)
        }
    }


    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        for (service in manager!!.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun onUploadButtonClicked() {
        // もしオブジェクト近くのボタンが true なら、false に設定する
        if (nearObjectButtonChecked.value) {
            nearObjectButtonChecked.value = false
        }
        uploadButtonChecked.value = !uploadButtonChecked.value
        if (uploadButtonChecked.value) {

            // サービスを停止
            context.stopService(serviceIntent)
        }
        if (!uploadButtonChecked.value) {
            selectedImageBitmapState.value = null
            if (!isServiceRunning(SpotScanService::class.java)) {
                context.startService(serviceIntent)
            }
            Menus.uploadpage = 0
            Menus.formMap.clear()
            Menus.formMap.apply {
                put("university", "")
                put("undergraduate", "")
                put("department", "")
                put("major", "")
                put("laboratory", "")
                put("location", "")
                put("roomNum", "")
                put("latitude", "")
                put("longitude", "")
            }
            stopCountDown()
            cancelScan()
        }

        Log.d("up", uploadButtonChecked.value.toString())
    }

    fun onNearObjectButtonClicked() {
        // もしアップロードボタンが true なら、false に設定する
        if (uploadButtonChecked.value) {
            if (!isServiceRunning(SpotScanService::class.java)) {
                context.startService(serviceIntent)
            }
            uploadButtonChecked.value = false
            selectedImageBitmapState.value = null
            Menus.uploadpage = 0
            Menus.formMap.clear()
            Menus.formMap.apply {
                put("university", "")
                put("undergraduate", "")
                put("department", "")
                put("major", "")
                put("laboratory", "")
                put("location", "")
                put("roomNum", "")
                put("latitude", "")
                put("longitude", "")
            }
            stopCountDown()
            cancelScan()
        }
        nearObjectButtonChecked.value = !nearObjectButtonChecked.value
        Log.d("near", nearObjectButtonChecked.value.toString())
    }

    @Composable
    fun Buttonlayout() {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.Transparent)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .background(color = Color.Transparent)
            ) {
                Buttons.UploadMenuButton()
                Buttons.NearObjectMenuButton()
            }
        }
    }

    @Composable
    fun Menulayout() {
        if (uploadButtonChecked.value || nearObjectButtonChecked.value) {
            Menus.Menu()
        }
    }

    @Composable
    fun Searchlayout() {
        if (searchobject.value != null
            && !uploadButtonChecked.value
            && !nearObjectButtonChecked.value
        ) {
            if (searchobject.value!!.id== spotObject.value.keys.toString()){
                searchobject.value=null
            }


            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            )
            {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                )
                {
                    Box(
                        modifier = Modifier
                            .height(LocalConfiguration.current.screenHeightDp.dp / 5.5f) // 画面の高さ/6
                            .fillMaxWidth(0.9f)
                            .background(
                                color = Color.White.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(15.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(0.dp, LocalConfiguration.current.screenHeightDp.dp / 200)
                                .height(LocalConfiguration.current.screenHeightDp.dp / 6) // 画面の高さ/6
                                .fillMaxWidth(0.95f)
                                .border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Box(modifier = Modifier.weight(0.05f))
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(0.85f)
                                    .weight(1f)
                            ) {
                                AutoResizeText(
                                    modifier = Modifier.weight(1f),
                                    text = searchobject.value!!.university.name,
                                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                                )
                                AutoResizeText(
                                    modifier = Modifier.weight(1f),
                                    text = "${searchobject.value!!.university.undergraduate} ${searchobject.value!!.university.department}",
                                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                                )
                                AutoResizeText(
                                    modifier = Modifier.weight(1f),
                                    text = "${searchobject.value!!.laboratory.location} ${searchobject.value!!.laboratory.roomNum}",
                                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                                )
                                AutoResizeText(
                                    modifier = Modifier.weight(1f),
                                    text = searchobject.value!!.laboratory.name,
                                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.85f)
                                    .weight(0.3f),
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                Box(
                                    modifier = Modifier

                                        .fillMaxWidth(0.5f)
                                        .aspectRatio(1f)
                                        .background(
                                            color = Color.White.copy(alpha = 0.3f),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                        .border(1.dp, Color.Gray, RoundedCornerShape(5.dp))
                                        .clickable {
                                                   searchobject.value=null
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "閉じる",
                                        tint = Color(0xFF000000),
                                        modifier = Modifier.fillMaxSize(0.7f)
                                    )
                                }
                            }
                            Box(modifier = Modifier.weight(0.05f))

                        }
                    }
                }
            }
        }
    }
}