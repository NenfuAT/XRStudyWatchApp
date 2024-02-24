package com.k21091.xrstudywatchapp.view

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.k21091.xrstudywatchapp.R

var selectedImageBitmapState = mutableStateOf<ImageBitmap?>(null)
@Composable
    fun ImageSelectionScreen(selectedImageBitmap: ImageBitmap?) {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (selectedImageBitmap != null) {
                // 選択された画像を表示
                Image(
                    bitmap = selectedImageBitmap,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(10.dp)
                        ),
                    contentScale = ContentScale.Crop
                )
            }
            else{
                Icon(
                    painter = painterResource(R.drawable.image),
                    contentDescription = "画像",
                    tint = Color.Black,
                    modifier = Modifier.fillMaxSize(0.4f)
                )
            }
        }
    }