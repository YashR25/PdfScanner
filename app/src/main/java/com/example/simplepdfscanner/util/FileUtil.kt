package com.example.simplepdfscanner.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object FileUtil {

    private var filePath:String? = null

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun getFileProvider(context: Context): Uri? {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        filePath = "file:" + image.absolutePath
        // Save a file: path for use with ACTION_VIEW intents

        return FileProvider.getUriForFile(
            context,
            "com.example.simplepdfscanner.fileprovider",
            image
        )
    }

    fun getFilePath(): String?{
        return filePath
    }

    fun byteArrayToBitmap(byteArray:ByteArray?): Bitmap?{
        val bitmap = byteArray?.let {
            BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        }
        return bitmap
    }
}