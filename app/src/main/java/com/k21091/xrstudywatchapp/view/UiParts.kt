package com.k21091.xrstudywatchapp.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.k21091.xrstudywatchapp.R

class UiParts {
    @SuppressLint("ResourceType")
    @Composable
    fun UploadMenuButton(){
        val checked = remember { mutableStateOf(false) }
        IconToggleButton(
            checked = checked.value,
            onCheckedChange = { checked.value = it },
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.5f), shape = CircleShape
            )
        )
        {
            if (checked.value) Icon(Icons.Default.Clear,contentDescription = "閉じる",tint = Color(0xFF000000))
            else Icon(painter = painterResource(R.drawable.cloudupload), contentDescription = "アップロード",tint = Color(0xFF000000))
        }
    }
    @Composable
    fun NearObjectMenuButton(){
        val checked = remember { mutableStateOf(false) }
        IconToggleButton(
            checked = checked.value,
            onCheckedChange = { checked.value = it },
            modifier = Modifier.background(
                color = Color.White.copy(alpha = 0.5f), shape = CircleShape
            )
        )
        {
            if (checked.value) Icon(Icons.Default.Clear,contentDescription = "閉じる",tint = Color(0xFF000000))
            else Icon(painter = painterResource(R.drawable.kid_star), contentDescription = "アップロード",tint = Color(0xFF000000))
        }
    }
}