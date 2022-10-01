package com.example.simplepdfscanner.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    suspend fun toGrayScale(bitmap: Bitmap):Bitmap{
        return withContext(Dispatchers.IO){
            val width = bitmap.width
            val height = bitmap.height

            val bmpGrayScale = Bitmap.createBitmap(width,height,Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmpGrayScale)
            val paint = Paint()
            val colorMatrix = ColorMatrix()
            colorMatrix.setSaturation(0F)
            val colorMatrixFilter = ColorMatrixColorFilter(colorMatrix)
            paint.colorFilter = colorMatrixFilter
            canvas.drawBitmap(bitmap,null, Rect(0,0,bitmap.width,bitmap.height),paint)
            bmpGrayScale
        }
    }

    suspend fun toWhiteBoard(bitmap: Bitmap):Bitmap{
        return withContext(Dispatchers.IO){
            val bwBitmap = Bitmap.createBitmap(
                bitmap.width,
                bitmap.height,
                Bitmap.Config.RGB_565
            )
            val hsv = FloatArray(3)
            for (col in 0 until bitmap.width) {
                for (row in 0 until bitmap.height) {
                    Color.colorToHSV(bitmap.getPixel(col, row), hsv)
                    if (hsv[2] > 0.5f) {
                        bwBitmap.setPixel(col, row, -0x1)
                    } else {
                        bwBitmap.setPixel(col, row, -0x1000000)
                    }
                }
            }
            bwBitmap
        }
    }

    suspend fun bitmapToWhiteBoard2(bitmap: Bitmap):Bitmap{
        return withContext(Dispatchers.IO){
        val bwBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bwBitmap)
        val contrastMatrix = ColorMatrix()
        val contrast = 50f
        val shift = (-.5f * contrast + .5f) * 255f
        contrastMatrix.set(
            floatArrayOf(
                contrast, 0f, 0f, 0f, shift,
                0f, contrast, 0f, 0f, shift,
                0f, 0f, contrast, 0f, shift,
                0f, 0f, 0f, 1f, 0f
            )
        )
        val contrastPaint = Paint()
        contrastPaint.colorFilter = ColorMatrixColorFilter(contrastMatrix)
        canvas.drawBitmap(bitmap, null, Rect(0,0,bitmap.width,bitmap.height), contrastPaint)
//        val saturationMatrix = ColorMatrix()
//        saturationMatrix.setSaturation(0f) //you set color saturation to 0 for b/w
//        val saturationPaint = Paint()
//        saturationPaint.colorFilter = ColorMatrixColorFilter(saturationMatrix)
//        canvas.drawBitmap(bitmap, null, Rect(0,0,bitmap.width,bitmap.height), saturationPaint)
        bwBitmap
        }
    }


}