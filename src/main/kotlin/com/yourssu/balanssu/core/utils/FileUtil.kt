package com.yourssu.balanssu.core.utils

import org.apache.commons.io.FileUtils
import java.io.File
import java.util.Base64

@Deprecated("더이상 선택지를 표현하는 이미지를 업로드하지 않습니다.")
object FileUtil {
    fun convertBase64ToImage(base64: String, image: File) {
        val bytes = Base64.getDecoder().decode(base64)
        FileUtils.writeByteArrayToFile(image, bytes)
    }
}
