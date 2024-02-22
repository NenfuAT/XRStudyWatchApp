package com.k21091.xrstudywatchapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.k21091.xrstudywatchapp.R
import kotlin.math.roundToInt

class ButtonParts (private val ui: UiView) {
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
class MenuParts(private val ui: UiView){
    @SuppressLint("NotConstructor")
    @Composable
    fun UploadMenu(){
        val screenHeight = LocalContext.current.resources.displayMetrics.heightPixels
        val maxOffsetY =0f
        val minOffsetY =(screenHeight * -0.7f)
        var offsetY by remember { mutableStateOf(0f) }  //コンテンツのOffsetを変更する用

        Box(modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(0, offsetY.roundToInt()) }
            ,
            contentAlignment = Alignment.Center){
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.7f)
                    .fillMaxWidth(0.8f)
            ){
                Box(modifier = Modifier
                    .background(
                        color = Color.White.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(15.dp)
                    )
                    .fillMaxWidth()
                    .fillMaxHeight(0.1f),
                    contentAlignment = Alignment.Center
                ){
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.02f))
                Column(
                    modifier = Modifier
                        .background(
                            color = Color.White.copy(alpha = 0.8f),
                            shape = RoundedCornerShape(15.dp)
                        )
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.04f))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.9f)
                            .fillMaxWidth(0.9f)
                            .border(2.dp, Color.Black, RoundedCornerShape(15.dp))
                    ){
                        //karamuyouso
                    }
                    Box(
                        modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    )
                    {
                        Box (
                            modifier = Modifier
                                .fillMaxHeight(0.15f)
                                .fillMaxWidth(0.35f)
                                .background(Color(0xFF666666), RoundedCornerShape(15.dp))
                                .draggable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    state = rememberDraggableState { delta ->
                                        val proposedOffsetY = offsetY + delta
                                        offsetY = proposedOffsetY.coerceIn(minOffsetY, maxOffsetY)
                                    },
                                    onDragStopped = {
                                        if (offsetY<screenHeight * -0.6f){
                                            ui.onUploadButtonClicked()
                                            }
                                        else{
                                            offsetY = maxOffsetY
                                            }
                                    },
                                    orientation = Orientation.Vertical,
                                )
                        )
                    }

                }

            }
        }
    }
}