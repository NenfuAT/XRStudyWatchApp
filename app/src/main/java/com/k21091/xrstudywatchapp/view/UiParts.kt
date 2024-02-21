package com.k21091.xrstudywatchapp.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.k21091.xrstudywatchapp.R

class UiParts (private val ui: UiView){
    @SuppressLint("ResourceType")
    @Composable
    fun UploadMenuButton(){
        IconToggleButton(
            checked = remember {
                ui.uploadButtonChecked.value
            },
            onCheckedChange = { ui.onUploadButtonClicked() },
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.5f), shape = CircleShape
            )
        )
        {
            if (ui.uploadButtonChecked.value) {
                Icon(Icons.Default.Clear,contentDescription = "閉じる",tint = Color(0xFF000000))
            }
            else Icon(painter = painterResource(R.drawable.cloudupload), contentDescription = "アップロード",tint = Color(0xFF000000))
        }
    }
    @Composable
    fun NearObjectMenuButton(){
        IconToggleButton(
            checked = remember {
                ui.nearObjectButtonChecked.value
            },
            onCheckedChange = { ui.onNearObjectButtonClicked()
            },
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.5f), shape = CircleShape
            )
        )
        {
            if (ui.nearObjectButtonChecked.value){
                Icon(Icons.Default.Clear,contentDescription = "閉じる",tint = Color(0xFF000000))
            }
            else Icon(painter = painterResource(R.drawable.kid_star), contentDescription = "アップロード",tint = Color(0xFF000000))
        }
    }
}