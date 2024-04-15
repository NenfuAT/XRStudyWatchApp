package com.k21091.xrstudywatchapp.view

import Count1Min
import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k21091.xrstudywatchapp.R
import com.k21091.xrstudywatchapp.service.latitude
import com.k21091.xrstudywatchapp.service.longitude
import com.k21091.xrstudywatchapp.util.CreateCsv
import com.k21091.xrstudywatchapp.util.WifiData
import com.k21091.xrstudywatchapp.util.areaObject
import com.k21091.xrstudywatchapp.util.buildJsonDataBody
import com.k21091.xrstudywatchapp.util.buildMultipartFormDataBody
import com.k21091.xrstudywatchapp.util.imageFilePath
import com.k21091.xrstudywatchapp.util.requestBodyToString
import com.k21091.xrstudywatchapp.util.searchobject
import com.k21091.xrstudywatchapp.util.sendRequest
import com.k21091.xrstudywatchapp.util.uploadFilePath
import com.k21091.xrstudywatchapp.util.userId
import kotlinx.serialization.Serializable
import org.json.JSONException
import org.json.JSONObject

import kotlin.math.roundToInt


@Composable
fun LoginHome(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(0.9f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.weight(1f))
        AutoResizeText(
            modifier = Modifier.weight(2f),
            text = "研究活動\nWatch",
            fontSizeRange = FontSizeRange(min = 30.sp, max = 50.sp),
            textAlign = TextAlign.Center
        )
        Box(modifier = Modifier.weight(2f))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.7f)
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(30.dp)
                )
                .clickable { loginButtonChecked.value = true }
        ) {
            AutoResizeText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.6f),
                text = "ログイン",
                fontSizeRange = FontSizeRange(min = 20.sp, max = 30.sp),
                textAlign = TextAlign.Center
            )
        }
        Box(modifier = Modifier.weight(0.1f))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.7f)
                .background(
                    Color.LightGray,
                    shape = RoundedCornerShape(30.dp)
                )
                .clickable { createUserButtonChecked.value = true }
        ) {
            AutoResizeText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.6f),
                text = "ユーザ登録",
                fontSizeRange = FontSizeRange(min = 20.sp, max = 30.sp),
                textAlign = TextAlign.Center
            )
        }
        Box(modifier = Modifier.weight(0.1f))
        Box(
            modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(0.7f)
        ) {
            AutoResizeText(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                text = "アプリケーションについて",
                fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LoginMenu(
    toMain: () -> Unit,
    modifier: Modifier = Modifier
) {
    var loginResult by remember { mutableStateOf("") }
    val formMap by remember { mutableStateOf(mutableMapOf("email" to "", "password" to "")) }


    Column(
        modifier = modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth(0.9f)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(15.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { loginButtonChecked.value = false }
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Back",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Box(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
        ) {
            AutoResizeText(
                text = loginResult,
                fontSizeRange = FontSizeRange(min = 20.sp, max = 50.sp)
            )
            if (loginResult == "complete") {
                toMain()
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        )
        {

            Box(modifier = Modifier.weight(0.2f))
            var Email by remember { mutableStateOf("") }
            AutoResizeText(
                modifier = Modifier.weight(0.5f),
                text = "メールアドレス",
                fontSizeRange = FontSizeRange(min = 20.sp, max = 50.sp),
            )
            SearchTextField(
                value = Email,
                onValueChange = {
                    Email = it
                    formMap["email"] = it
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            )
            Box(modifier = Modifier.weight(0.2f))
            AutoResizeText(
                modifier = Modifier.weight(0.5f),
                text = "パスワード",
                fontSizeRange = FontSizeRange(min = 20.sp, max = 50.sp),
            )
            var Password by remember { mutableStateOf("") }
            SearchTextField(
                value = Password,
                onValueChange = {
                    Password = it
                    formMap["password"] = it
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        ) {
            Box(modifier = Modifier
                .fillMaxHeight(0.5f)
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(10.dp))
                .align(Alignment.Center)
                .clickable {
                    Log.d("form", "$formMap")
                    var body = buildJsonDataBody(formMap)

                    Log.d("body", requestBodyToString(body))
                    sendRequest(body, "", "api/user/login",
                        onResponse = { response ->
                            // レスポンスが正常に受信された場合の処理
                            // ここで必要な処理を行います

                            try {
                                val jsonResponse = JSONObject(response.toString())
                                val error = jsonResponse.optString("error")
                                if (error.isNotEmpty()) {
                                    println("Error received: $error")
                                    loginResult = error
                                } else {
                                    val id = jsonResponse.optString("id")
                                    userId.value = id
                                    println("Response received: $userId")
                                    loginResult = "complete"
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                // JSONのパースエラーが発生した場合の処理
                            }

                        },
                        onFailure = { errorMessage ->
                            // エラーが発生した場合の処理
                            println("Request failed with exception: $errorMessage")
                            loginResult = "404NotFound"
                        }
                    )
                }
            )
            {
                AutoResizeText(
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                        .align(Alignment.Center),
                    text = "ログイン",
                    fontSizeRange = FontSizeRange(min = 30.sp, max = 60.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun CreateUserMenu(modifier: Modifier = Modifier) {
    var createResult by remember { mutableStateOf("") }
    var formState by remember { mutableStateOf(false) }
    var male by remember { mutableStateOf(false) }
    var female by remember { mutableStateOf(false) }
    var other by remember { mutableStateOf(false) }

    @Serializable
    data class UserData(
        var name: String? = null,
        var email: String? = null,
        var gender: String? = null,
        var age: Int? = null,
        var height: Int? = null,
        var weight: Float? = null,
        var occupation: String? = null,
        var address: String? = null,
        var password: String? = null
    )

    var Name by remember { mutableStateOf("") }
    var Occupation by remember { mutableStateOf("") }
    var Email by remember { mutableStateOf("") }
    var Age by remember { mutableStateOf("") }
    var Height by remember { mutableStateOf("") }
    var Weight by remember { mutableStateOf("") }
    var Address by remember { mutableStateOf("") }
    var Password by remember { mutableStateOf("") }
    val userData = UserData()


    Column(
        modifier = modifier
            .fillMaxHeight(0.7f)
            .fillMaxWidth(0.9f)
            .background(
                color = Color.LightGray,
                shape = RoundedCornerShape(15.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth()
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = { createUserButtonChecked.value = false }
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Back",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Box(modifier = Modifier.weight(0.5f)) {
            AutoResizeText(
                text = createResult,
                fontSizeRange = FontSizeRange(min = 1.sp, max = 20.sp)
            )
            if (createResult == "complete") {
                createUserButtonChecked.value = false
            }
        }
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        )
        {
            Column(modifier = Modifier.weight(1f)) {
                AutoResizeText(
                    modifier = Modifier.weight(0.5f),
                    text = "名前",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
                )
                SearchTextField(
                    value = Name,
                    onValueChange = {
                        Name = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )
            }
            Box(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier.weight(1f)) {
                AutoResizeText(
                    modifier = Modifier.weight(0.5f),
                    text = "職業",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
                )

                SearchTextField(
                    value = Occupation,
                    onValueChange = {
                        Occupation = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )
            }

        }
        Box(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        ) {
            AutoResizeText(
                modifier = Modifier.weight(0.5f),
                text = "メールアドレス",
                fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
            )

            SearchTextField(
                value = Email,
                onValueChange = {
                    Email = it
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            )
        }
        Box(modifier = Modifier.weight(0.1f))
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        )
        {
            Column(modifier = Modifier.weight(1f)) {
                AutoResizeText(
                    modifier = Modifier.weight(0.5f),
                    text = "年齢",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
                )

                SearchTextField(
                    value = Age,
                    onValueChange = {
                        Age = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )
            }
            Box(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier.weight(1f)) {
                AutoResizeText(
                    modifier = Modifier.weight(0.5f),
                    text = "身長",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
                )

                SearchTextField(
                    value = Height,
                    onValueChange = {
                        Height = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )


            }
            Box(modifier = Modifier.weight(0.1f))
            Column(modifier = Modifier.weight(1f)) {
                AutoResizeText(
                    modifier = Modifier.weight(0.5f),
                    text = "体重",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
                )

                SearchTextField(
                    value = Weight,
                    onValueChange = {
                        Weight = it
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )


            }
        }
        Box(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        ) {
            AutoResizeText(
                modifier = Modifier.weight(0.5f),
                text = "性別",
                fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
            )
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = male, onClick =
                            {
                                male = true
                                female = false
                                other = false
                            }, modifier = Modifier.weight(0.5f)
                        )
                        AutoResizeText(
                            modifier = Modifier.weight(1f),
                            text = ":男性",
                            fontSizeRange = FontSizeRange(min = 1.sp, max = 20.sp)
                        )
                    }

                }
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = female, onClick =
                            {
                                male = false
                                female = true
                                other = false
                            }, modifier = Modifier.weight(0.5f)
                        )
                        AutoResizeText(
                            modifier = Modifier.weight(1f),
                            text = ":女性",
                            fontSizeRange = FontSizeRange(min = 1.sp, max = 20.sp)
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        RadioButton(
                            selected = other, onClick =
                            {
                                male = false
                                female = false
                                other = true

                            }, modifier = Modifier.weight(0.5f)
                        )
                        AutoResizeText(
                            modifier = Modifier.weight(1f),
                            text = ":その他",
                            fontSizeRange = FontSizeRange(min = 1.sp, max = 20.sp),
                            maxLines = 1
                        )
                    }
                }

            }


        }

        Box(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        ) {
            AutoResizeText(
                modifier = Modifier.weight(0.5f),
                text = "住所",
                fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
            )

            SearchTextField(
                value = Address,
                onValueChange = {
                    Address = it
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            )
        }
        Box(modifier = Modifier.weight(0.1f))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(0.9f)
        ) {
            AutoResizeText(
                modifier = Modifier.weight(0.5f),
                text = "パスワード",
                fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp)
            )

            SearchTextField(
                value = Password,
                onValueChange = {
                    Password = it
                },
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color.Gray.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
            )
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth(0.9f)
        )
        {
            Box(modifier = Modifier
                .fillMaxHeight(0.6f)
                .fillMaxWidth()
                .background(Color.Gray, shape = RoundedCornerShape(30.dp))
                .align(Alignment.Center)
                .clickable {
                    userData.name = Name
                    userData.occupation = Occupation
                    userData.email = Email
                    if (Age.isNotEmpty()) {
                        userData.age = Age.toInt()
                    }
                    if (Height.isNotEmpty()) {
                        userData.height = Height.toInt()
                    }
                    if (Weight.isNotEmpty()) {
                        userData.weight = Weight.toFloat()
                    }
                    if (male) {
                        userData.gender = "male"
                    }
                    if (female) {
                        userData.gender = "female"
                    }
                    if (other) {
                        userData.gender = "other"
                    }
                    userData.address = Address
                    userData.password = Password



                    Log.d("form", "$userData")
                    val body = buildJsonDataBody(userData)

                    Log.d("body", requestBodyToString(body))
                    sendRequest(body, "", "api/user/create",
                        onResponse = { response ->
                            // レスポンスが正常に受信された場合の処理
                            // ここで必要な処理を行います

                            try {
                                val jsonResponse = JSONObject(response.toString())
                                val error = jsonResponse.optString("error")
                                if (error.isNotEmpty()) {
                                    println("Error received: $error")
                                    createResult = error
                                } else {
                                    val id = jsonResponse.optString("id")
                                    println("Response received: $id")
                                    createResult = "complete"
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                                // JSONのパースエラーが発生した場合の処理
                            }

                        },
                        onFailure = { errorMessage ->
                            // エラーが発生した場合の処理
                            println("Request failed with exception: $errorMessage")
                            createResult = "404NotFound"
                        }
                    )
                }
            )
            {
                AutoResizeText(
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                        .align(Alignment.Center),
                    text = "登録",
                    fontSizeRange = FontSizeRange(min = 30.sp, max = 60.sp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


class ButtonParts(private val ui: UiView) {
    val iconModifier = Modifier
        .padding(top = 1.dp)
        .fillMaxSize(0.8f)

    @SuppressLint("ResourceType")
    @Composable
    fun UploadMenuButton() {
        IconToggleButton(
            checked = remember {
                ui.uploadButtonChecked.value
            },
            onCheckedChange = { ui.onUploadButtonClicked() },
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.8f), shape = CircleShape
            )
        )
        {
            if (ui.uploadButtonChecked.value) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "閉じる",
                    tint = Color(0xFF000000),
                    modifier = iconModifier
                )
            } else Icon(
                painter = painterResource(R.drawable.cloudupload),
                contentDescription = "アップロード",
                tint = Color(0xFF000000),
                modifier = iconModifier
            )
        }
    }

    @Composable
    fun NearObjectMenuButton() {
        var areaobject= remember {
            areaObject
        }
        IconToggleButton(
            checked = remember {
                ui.nearObjectButtonChecked.value
            },
            onCheckedChange = {
                ui.onNearObjectButtonClicked()
            },
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.8f), shape = CircleShape
            )
        )
        {
            if (ui.nearObjectButtonChecked.value) {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "閉じる",
                    tint = Color(0xFF000000),
                    modifier = iconModifier
                )
            } else
                Box(modifier = Modifier){
                    Icon(
                        painter = painterResource(R.drawable.kid_star),
                        contentDescription = "アップロード",
                        tint = Color(0xFF000000),
                        modifier = iconModifier
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(top = 1.dp),
                        text = "${areaobject.value.size}"
                    )
                }
        }
    }
}

class MenuParts(private val ui: UiView, var getContent: ActivityResultLauncher<String>) {
    var uploadpage by mutableStateOf(0)
    var uploadResult by mutableStateOf("")

    val formMap = mutableMapOf<String, String>().apply {
        put("university", "ホゲ大学")
        put("undergraduate", "ホゲ学部")
        put("department", "ホゲ学科")
        put("major", "ホゲ専攻")
        put("laboratory", "ホゲ研究室")
        put("location", "ほげ館")
        put("roomNum", "101s")
        put("latitude", "35.681236")
        put("longitude", "-139.767125")
    }

    val fileList: MutableList<Pair<String, String>> = mutableListOf()

    @SuppressLint("NotConstructor")
    @Composable
    fun Menu() {
        val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
        val maxOffsetY = 0f
        val minOffsetY = (screenHeight * -0.7f)
        var offsetY by remember { mutableStateOf(0f) }  //コンテンツのOffsetを変更する用
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offsetY.roundToInt()) },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .fillMaxWidth(0.9f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.04f)
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .fillMaxWidth()
                        .fillMaxHeight(0.1f),
                    contentAlignment = Alignment.Center
                ) {
                    if (ui.uploadButtonChecked.value) {
                        AutoResizeText(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(0.95f),
                            text = "研究室情報をアップロード",
                            fontSizeRange = FontSizeRange(min = 20.sp, max = 30.sp),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                    if (ui.nearObjectButtonChecked.value) {
                        AutoResizeText(
                            modifier = Modifier
                                .wrapContentHeight()
                                .fillMaxWidth(0.95f),
                            text = "近くの研究室ポスター",
                            fontSizeRange = FontSizeRange(min = 20.sp, max = 30.sp),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.02f)
                )
                Column(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.02f)
                    )
                    if (ui.uploadButtonChecked.value) {
                        if (uploadpage == 0) {
                            UploadMenu(getContent)
                        } else if (uploadpage == 1) {
                            UploadMenu(getContent)
                        }
                    }
                    if (ui.nearObjectButtonChecked.value) {
                        NearObjectMenu()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.35f)
                                .draggable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    state = rememberDraggableState { delta ->
                                        val proposedOffsetY = offsetY + delta
                                        offsetY = proposedOffsetY.coerceIn(minOffsetY, maxOffsetY)
                                    },
                                    onDragStopped = {
                                        if (offsetY < screenHeight * -0.1f) {
                                            if (ui.uploadButtonChecked.value) {
                                                ui.onUploadButtonClicked()
                                            }
                                            if (ui.nearObjectButtonChecked.value) {
                                                ui.onNearObjectButtonClicked()
                                            }

                                        } else {
                                            offsetY = maxOffsetY
                                        }
                                    },
                                    orientation = Orientation.Vertical
                                ),
                            contentAlignment = Alignment.Center
                        )
                        {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.2f)
                                    .fillMaxWidth()
                                    .background(Color(0xFF666666), RoundedCornerShape(15.dp)),
                            )
                        }
                    }

                }

            }
        }

    }

    @Composable
    fun UploadMenu(
        getContent: ActivityResultLauncher<String>
    ) {

        val uploadResult= remember {
            uploadResult
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth(0.95f)
                .border(2.dp, Color.Black, RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uploadpage == 0) {

                Box(modifier = Modifier.weight(0.1f))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "大学名",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var University by remember { mutableStateOf("") }
                        SearchTextField(
                            value = University,
                            onValueChange = {
                                University = it
                                formMap["university"] = University
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }

                }


                Box(modifier = Modifier.weight(0.1f))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "学部",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var Undergraduate by remember { mutableStateOf("") }
                        SearchTextField(
                            value = Undergraduate,
                            onValueChange = {
                                Undergraduate = it
                                formMap["undergraduate"] = Undergraduate
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }
                    Box(modifier = Modifier.weight(0.05f))
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "学科",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var Department by remember { mutableStateOf("") }
                        SearchTextField(
                            value = Department,
                            onValueChange = {
                                Department = it
                                formMap["department"] = Department
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }


                }
                Box(modifier = Modifier.weight(0.1f))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "専攻",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var Major by remember { mutableStateOf("") }
                        SearchTextField(
                            value = Major,
                            onValueChange = {
                                Major = it
                                formMap["major"] = Major
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }

                }
                Box(modifier = Modifier.weight(0.1f))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "研究室名",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var Laboratory by remember { mutableStateOf("") }
                        SearchTextField(
                            value = Laboratory,
                            onValueChange = {
                                Laboratory = it
                                formMap["laboratory"] = Laboratory
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }

                }
                Box(modifier = Modifier.weight(0.1f))
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "建物",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var Location by remember { mutableStateOf("") }
                        SearchTextField(
                            value = Location,
                            onValueChange = {
                                Location = it
                                formMap["location"] = Location
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }
                    Box(modifier = Modifier.weight(0.05f))
                    Column(modifier = Modifier.weight(1f)) {
                        AutoResizeText(
                            modifier = Modifier
                                .weight(0.6f),
                            text = "部屋番号",
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                            textAlign = TextAlign.Left,
                            maxLines = 1
                        )
                        var RoomNum by remember { mutableStateOf("") }
                        SearchTextField(
                            value = RoomNum,
                            onValueChange = {
                                RoomNum = it
                                formMap["roomNum"] = RoomNum
                            },
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    color = Color.Gray.copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                        )
                    }


                }
                Box(modifier = Modifier.weight(0.1f))
                Column(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(0.9f)
                ) {
                    AutoResizeText(
                        modifier = Modifier
                            .weight(0.14f),
                        text = "研究室紹介資料(.png,.jpgのみ)",
                        fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                        textAlign = TextAlign.Left,
                        maxLines = 1
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(
                                color = Color.Gray.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                            .clickable {
                                getContent.launch("image/*")
                            }
                    )
                    {
                        ImageSelectionScreen(selectedImageBitmapState.value)
                    }
                }

                Box(modifier = Modifier.weight(0.2f))
                Box(
                    modifier = Modifier
                        .weight(1.2f)
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color.White.copy(alpha = 0.4f),
                            RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, RoundedCornerShape(10.dp))
                        .clickable
                        {
                            uploadpage = 1
                        },
                    contentAlignment = Alignment.Center
                )
                {
                    AutoResizeText(
                        modifier = Modifier
                            .fillMaxHeight(0.5f)
                            .fillMaxWidth(0.3f),
                        text = "NEXT",
                        fontSizeRange = FontSizeRange(min = 10.sp, max = 30.sp),
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
                Box(modifier = Modifier.weight(0.2f))

            } else if (uploadpage == 1) {
                Box(modifier = Modifier.weight(0.1f))
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxWidth(0.9f),
                    contentAlignment = Alignment.Center
                ) {
                    AutoResizeText(
                        modifier = Modifier
                            .fillMaxHeight(0.9f)
                            .fillMaxWidth(),
                        text = "1分間表示させたいエリア\nを歩いてください",
                        fontSizeRange = FontSizeRange(min = 10.sp, max = 50.sp),
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(0.7f)
                        .fillMaxWidth(0.9f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(1f)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize(),
                            onDraw = {
                                drawArc(
                                    color = Color.Black,
                                    startAngle = -40f, // 調整
                                    sweepAngle = 260f, // 調整
                                    useCenter = false,
                                    topLeft = Offset(0F, (size.height / 3) * 2),
                                    size = Size(size.width, size.height / 3),
                                    style = Stroke(
                                        width = size.height * 0.04f,
                                        cap = StrokeCap.Round, // 角を丸くする
                                    ),
                                )
                            }
                        )
                        Icon(
                            modifier = Modifier.fillMaxSize(0.9f),
                            painter = painterResource(R.drawable.walk), contentDescription = "歩く",
                            tint = Color.Black
                        )
                    }

                }
                Column(
                    modifier = Modifier
                        .weight(1.5f)
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally
                )


                {
                    Box(modifier = Modifier.weight(0.05f))
                    CountDownCanvas(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f),
                        getContent,
                        formMap,
                        fileList,
                        ui
                    )
                    Box(modifier = Modifier.weight(0.5f))
                    {

                        AutoResizeText(
                            modifier = Modifier
                                .fillMaxHeight(0.9f)
                                .fillMaxWidth(),
                            text = uploadResult,
                            fontSizeRange = FontSizeRange(min = 10.sp, max = 50.sp),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }

            }
        }


    }

    @Composable
    fun NearObjectMenu() {
        var nowareaObject by remember { areaObject }
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth(0.95f)
        ) {
            items(nowareaObject.keys.size) { index ->
                val key = nowareaObject.keys.toList()[index] // インデックスを使用してキーを取得します
                val areaobject = nowareaObject[key] // キーを使用してareaObjectから値を取得します
                if (areaobject != null) {
                    println(areaobject)
                }
                Row(
                    modifier = Modifier
                        .padding(0.dp, LocalConfiguration.current.screenHeightDp.dp / 200)
                        .height(LocalConfiguration.current.screenHeightDp.dp / 6) // 画面の高さ/6
                        .fillMaxWidth()
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
                        if (areaobject != null) {
                            AutoResizeText(
                                modifier = Modifier.weight(1f),
                                text = areaobject.university.name,
                                fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                            )
                            AutoResizeText(
                                modifier = Modifier.weight(1f),
                                text = "${areaobject.university.undergraduate} ${areaobject.university.department}",
                                fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                            )
                            AutoResizeText(
                                modifier = Modifier.weight(1f),
                                text = "${areaobject.laboratory.location} ${areaobject.laboratory.roomNum}",
                                fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                            )
                            AutoResizeText(
                                modifier = Modifier.weight(1f),
                                text = areaobject.laboratory.name,
                                fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp)
                            )
                        }

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
                                    searchobject.value = areaobject
                                    ui.onNearObjectButtonClicked()
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "探す",
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

@Composable
fun CountDownCanvas(
    modifier: Modifier = Modifier,
    getContent: ActivityResultLauncher<String>,
    formMap: MutableMap<String, String>,
    fileList: MutableList<Pair<String, String>>,
    ui: UiView
) {
    val context = LocalContext.current
    val state = remember { mutableStateOf(0) }
    val count1m = remember { Count1Min() }
    val seconds = remember { mutableStateOf(60f) }
    Box(modifier = modifier, Alignment.Center) {
        if (state.value == 0) {
            AutoResizeText(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(0.8f),
                text = "start",
                fontSizeRange = FontSizeRange(min = 50.sp, max = 100.sp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
        if (state.value == 1) {
            AutoResizeText(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth(0.8f),
                text = "${seconds.value.toInt()}",
                fontSizeRange = FontSizeRange(min = 50.sp, max = 100.sp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
        if (state.value == 2) {
            AutoResizeText(
                modifier = Modifier
                    .fillMaxHeight(0.4f)
                    .fillMaxWidth(0.8f),
                text = "finish",
                fontSizeRange = FontSizeRange(min = 30.sp, max = 80.sp),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize(0.9f)
                .clickable {
                    if (state.value == 0) {
                        count1m.startCountDown {
                            seconds.value = 60 - (it / 1000)
                            if (seconds.value <= 0) {
                                state.value = 2
                            }
                        }
                        state.value = 1
                        CreateCsv(context, 30, "upload").createcsvdata {
                            if (ui.uploadButtonChecked.value) {

                                formMap["latitude"] = latitude
                                formMap["longitude"] = longitude
                                fileList.add(Pair("rawDataFile", uploadFilePath.value))
                                fileList.add(Pair("objectFile", imageFilePath.value))
                                var body = buildMultipartFormDataBody(formMap, fileList)

                                sendRequest(body, userId.value, "api/object/create",
                                  onResponse = { response ->
                                        // レスポンスが正常に受信された場合の処理
                                        println("Response received: $response")
                                        //File(csvFilePath.value).delete()
                                        // ここで必要な処理を行います
                                    }
                                ) { errorMessage ->
                                    // エラーが発生した場合の処理
                                    println("Request failed with exception: $errorMessage")
                                    //File(csvFilePath.value).delete()
                                    // ここでエラー処理を行います
                                }
                            }
                        }
                    }

                }
        )
        {
            drawArc(
                color = Color.Black,
                startAngle = -90f,
                sweepAngle = -360f,
                useCenter = false,
                style = Stroke(width = size.height * 0.01f, cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
            drawArc(
                color = Color.Black,
                startAngle = -90f,
                sweepAngle = -360f * (seconds.value / 60f),
                useCenter = false,
                style = Stroke(width = size.height * 0.04f, cap = StrokeCap.Round),
                size = Size(size.width, size.height)
            )
        }
    }
}

