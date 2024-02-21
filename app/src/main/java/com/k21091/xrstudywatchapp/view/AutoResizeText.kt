package com.k21091.xrstudywatchapp.view


import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizeText(
    modifier: Modifier = Modifier,
    text: String,
    fontSizeRange: FontSizeRange,
    color: Color = Color.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis, // あふれた場合は省略する
    softWrap: Boolean = true,
    style: TextStyle = LocalTextStyle.current,
) {
    var fontSizeValue by remember { mutableStateOf(fontSizeRange.max.value) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        maxLines = maxLines,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        style = style,
        fontSize = fontSizeValue.sp,
        onTextLayout = {
            if (it.didOverflowHeight && !readyToDraw) {
                val nextFontSizeValue = fontSizeValue - fontSizeRange.step.value
                if (nextFontSizeValue <= fontSizeRange.min.value) {
                    // 最小サイズに到達した場合、縮小を終了する。
                    fontSizeValue = fontSizeRange.min.value
                    readyToDraw = true
                } else {
                    // まだ領域より大きく、最小サイズに達していない場合は縮小を続ける
                    fontSizeValue = nextFontSizeValue
                }
            } else {
                // 最適なサイズに達した時にことを知らせる
                readyToDraw = true
            }
        },
        modifier = modifier.drawWithContent { if (readyToDraw) drawContent() }
    )
}

data class FontSizeRange(
    val min: TextUnit,
    val max: TextUnit,
    val step: TextUnit = DEFAULT_TEXT_STEP,
) {
    companion object {
        private val DEFAULT_TEXT_STEP = 1.sp
    }
}
