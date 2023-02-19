package com.yourssu.balanssu.core.utils

import org.apache.commons.io.FileUtils
import java.io.File
import java.util.Base64

object FileUtil {
    fun convertBase64ToImage(base64: String, image: File) {
        val bytes = Base64.getDecoder().decode(base64)
        FileUtils.writeByteArrayToFile(image, bytes)
    }
}
