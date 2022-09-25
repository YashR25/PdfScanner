package com.example.simplepdfscanner.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import com.example.simplepdfscanner.model.PdfModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PdfRepository {

    fun createPdf(bitmapList: List<Bitmap>,context: Context){

        val pdfDocument: PdfDocument = PdfDocument()

        for (bitmap in bitmapList){
            val paint = Paint()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width,bitmap.height,1).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,ByteArrayOutputStream())
            canvas.drawBitmap(bitmap,null,Rect(0,0,bitmap.width,bitmap.height),paint)
            pdfDocument.finishPage(page)

        }

        val sdf = SimpleDateFormat("dd.MM.yyyy hh:mm")
        sdf.timeZone = TimeZone.getTimeZone("Asia/Kolkata")
        val currentDateAndTime = sdf.format(Date())

        val filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val dir = File(filePath?.absolutePath + "/Demo")
        dir.mkdir()
        val pathName = "${System.currentTimeMillis()}" + ".pdf"
        val file = File(dir,pathName)
        val uri = file.absolutePath
        val outputStream = FileOutputStream(file)
        pdfDocument.writeTo(outputStream)
        Log.d("Repository",uri)
    }
}