package com.k21091.xrstudywatchapp.view

import Count1Min
import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k21091.xrstudywatchapp.R
import com.k21091.xrstudywatchapp.util.CreateCsv

import elapsedMilliseconds
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

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
            } else Icon(
                painter = painterResource(R.drawable.kid_star),
                contentDescription = "アップロード",
                tint = Color(0xFF000000),
                modifier = iconModifier
            )
        }
    }
}

class MenuParts(private val ui: UiView,var getContent:ActivityResultLauncher<String>) {
    var SearchTextField = SearchTextField()
    var uploadpage by mutableStateOf(0)
    @SuppressLint("NotConstructor")
    @Composable
    fun Menu() {
        val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
        val maxOffsetY = 0f
        val minOffsetY = (screenHeight * -0.7f)
        var offsetY by remember { mutableStateOf(0f) }  //コンテンツのOffsetを変更する用
        Box(modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, offsetY.roundToInt()) },
            contentAlignment = Alignment.Center) {
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
                            text = "オブジェクトをアップロード",
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
                            text = "近くのオブジェクト",
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
                        if (uploadpage==0) {
                            UploadMenu(getContent)
                        }
                        else if (uploadpage==1) {
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
        Column(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth(0.95f)
                .border(2.dp, Color.Black, RoundedCornerShape(10.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
        if (uploadpage==0){

                Box(modifier = Modifier.weight(0.2f))
                AutoResizeText(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth(0.9f),
                    text = "大学名",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
                var UniversityName by remember { mutableStateOf("") }
                SearchTextField.SearchTextField(
                    value = UniversityName,
                    onValueChange = { UniversityName = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )
                Box(modifier = Modifier.weight(0.2f))
                AutoResizeText(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth(0.9f),
                    text = "学部学科",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
                var AcademicDepartment by remember { mutableStateOf("") }
                SearchTextField.SearchTextField(
                    value = AcademicDepartment,
                    onValueChange = { AcademicDepartment = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )
                Box(modifier = Modifier.weight(0.2f))
                AutoResizeText(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth(0.9f),
                    text = "研究室名",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
                var LaboratoryName by remember { mutableStateOf("") }
                SearchTextField.SearchTextField(
                    value = LaboratoryName,
                    onValueChange = { LaboratoryName = it },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(0.9f)
                        .background(
                            color = Color.Gray.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(1.dp, Color.Gray, shape = RoundedCornerShape(10.dp))
                )
                Box(modifier = Modifier.weight(0.2f))
                AutoResizeText(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxWidth(0.9f),
                    text = "研究室紹介資料(.png,.jpgのみ)",
                    fontSizeRange = FontSizeRange(min = 10.sp, max = 20.sp),
                    textAlign = TextAlign.Left,
                    maxLines = 1
                )
                Box(
                    modifier = Modifier
                        .weight(5f)
                        .fillMaxWidth(0.9f)
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
                Box(modifier = Modifier.weight(0.2f))
                Box(
                    modifier = Modifier
                        .weight(1.5f)
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

            }
        else if(uploadpage==1){
            Box(modifier = Modifier.weight(0.1f))
            Box(modifier = Modifier
                .weight(0.5f)
                .fillMaxWidth(0.9f),
                contentAlignment = Alignment.Center
            ){
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
            Box(modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth(0.9f),
                contentAlignment = Alignment.Center
            ){
                Box(
                    modifier = Modifier
                        .fillMaxHeight(1f)
                        .aspectRatio(1f),
                    contentAlignment = Alignment.Center
                ){
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize(),
                        onDraw = {
                            drawArc(
                                color = Color.Black,
                                startAngle = -40f, // 調整
                                sweepAngle = 260f, // 調整
                                useCenter = false,
                                topLeft= Offset(0F,(size.height / 3)*2),
                                size = Size(size.width, size.height/3),
                                style = Stroke(
                                    width = size.height*0.04f,
                                    cap = StrokeCap.Round, // 角を丸くする
                                ),
                            )
                        }
                    )
                    Icon(
                        modifier=Modifier.fillMaxSize(0.9f),
                        painter = painterResource(R.drawable.walk), contentDescription = "歩く",
                        tint = Color.Black
                    )
                }

            }
            Column(modifier = Modifier
                .weight(1.5f)
                .fillMaxWidth(0.9f),
                horizontalAlignment = Alignment.CenterHorizontally
            )


            {
                Box(modifier = Modifier.weight(0.05f))
                CountDownCanvas(modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                    getContent)
                Box(modifier = Modifier.weight(0.5f))
            }
        }
        }


    }

    @Composable
    fun NearObjectMenu() {
        LazyColumn(
            modifier = Modifier
                .fillMaxHeight(0.95f)
                .fillMaxWidth(0.95f)
        ) {
            item() {
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .fillMaxWidth()

                ) {

                }
            }

        }
    }
}
@Composable
fun CountDownCanvas(modifier: Modifier = Modifier, getContent:ActivityResultLauncher<String>) {
    val context = LocalContext.current
    val state = remember { mutableStateOf(0) }
    val count1m = remember { Count1Min() }
    val seconds = remember { mutableStateOf(60f) }
    Box(modifier = modifier, Alignment.Center) {
        if (state.value==0){
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
        if (state.value==1){
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
        if (state.value==2){
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
                        var CreateCsv = CreateCsv(context)
                        state.value = 1
                        CreateCsv.createcsvdata{
                            CreateCsv.Savecsv()
                            state.value=2
                        }
                    }

                }
        )
        {
            drawArc(
                color = Color.Black,
                startAngle = -90f,
                sweepAngle = -360f ,
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

