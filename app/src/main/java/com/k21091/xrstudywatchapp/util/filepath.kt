package com.k21091.xrstudywatchapp.util

import android.net.Uri
import androidx.compose.runtime.mutableStateOf

var imageFilePath = mutableStateOf<String>("")
var imageFileName = mutableStateOf<String>("")
var csvFilePath = mutableStateOf<String>("")
var csvFileName = mutableStateOf<String>("")
var uploadFilePath = mutableStateOf<String>("")
var uploadFileName = mutableStateOf<String>("")

var userId = mutableStateOf<String>("")
var isServiceRunning = mutableStateOf<Boolean>(false)


data class Laboratory(
    val name: String,
    val location: String,
    val roomNum: String
)

data class University(
    val name: String,
    val undergraduate: String,
    val department: String,
    val major: String
)

data class AroundObject(
    val id: String,
    val laboratory: Laboratory,
    val university: University
)

data class ArrivingObject(
    val id: String,
    val width: String,
    val height: String,
    val size: String,
    val viewUrl: String
)


val jsonString = """
{
    "arrivingObjects": [
        {
            "id": "01HTSFXDKKZ7TGPGWXGBDFG9Z6",
            "width": 0.75006145,
            "height": 1,
            "size": "6101x8134",
            "viewUrl": "https://pve01-storage-server.karasuneo.com/applications/01F8VYXK67BGC1F9RP1E4S9YTV/objects/01HTSFXDKKZ7TGPGWXGBDFG9Z6.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=4SVrbRAaIzNOThRz%2F20240408%2Fap-northeast-1%2Fs3%2Faws4_request&X-Amz-Date=20240408T020302Z&X-Amz-Expires=1200&X-Amz-Signature=11cbb01b68d76e6fc4d6cc5e8d91511a5797688298319cdd01b284ffd65e489a&X-Amz-SignedHeaders=host&x-id=GetObject"
        }
    ],
    "aroundObjects": [
    {
      "id": "01GWKR0SQ0KD0QZ3SHEEAM8YDS",
      "laboratory": {
        "name": "梶研究室",
        "location": "4号別館",
        "roomNum": "104"
      },
      "university": {
        "name": "愛知工業大学",
        "undergraduate": "情報科学部",
        "department": "情報科学科",
        "major": "コンピュータシステム"
      }
    },
    {
      "id": "01GWKR0SQ0KD0QZ3SHEEAM8YDJ",
      "laboratory": {
        "name": "伊藤伸研究室",
        "location": "4号別館",
        "roomNum": "204"
      },
      "university": {
        "name": "愛知工業大学",
        "undergraduate": "情報科学部",
        "department": "情報科学科",
        "major": "コンピュータシステム"
      }
    },
    {
      "id": "01GWKR0SQ0KD0QZ3SHEEAM8YDL",
      "laboratory": {
        "name": "梶研究室",
        "location": "4号別館",
        "roomNum": "104"
      },
      "university": {
        "name": "愛知工業大学",
        "undergraduate": "情報科学部",
        "department": "情報科学科",
        "major": "コンピュータシステム"
      }
    },
    {
      "id": "01GWKR0SQ0KD0QZ3SHEEAM8YDA",
      "laboratory": {
        "name": "伊藤伸研究室",
        "location": "4号別館",
        "roomNum": "204"
      },
      "university": {
        "name": "愛知工業大学",
        "undergraduate": "情報科学部",
        "department": "情報科学科",
        "major": "コンピュータシステム"
      }
    },
    {
      "id": "01GWKR0SQ0KD0QA3SHEEAM8YDS",
      "laboratory": {
        "name": "梶研究室",
        "location": "4号別館",
        "roomNum": "104"
      },
      "university": {
        "name": "愛知工業大学",
        "undergraduate": "情報科学部",
        "department": "情報科学科",
        "major": "コンピュータシステム"
      }
    },
    {
      "id": "01GWKR0SQ0KD0QZ3SHEESM8YDJ",
      "laboratory": {
        "name": "伊藤伸研究室",
        "location": "4号別館",
        "roomNum": "204"
      },
      "university": {
        "name": "愛知工業大学",
        "undergraduate": "情報科学部",
        "department": "情報科学科",
        "major": "コンピュータシステム"
      }
    }
  ]
}
""".trimIndent()



//スポット
var spotObject = mutableStateOf<Map<String, ArrivingObject>>(emptyMap())

//近く
var areaObject = mutableStateOf<Map<String, AroundObject>>(emptyMap())

//探してるやつ
var searchobject = mutableStateOf<AroundObject?>(null)