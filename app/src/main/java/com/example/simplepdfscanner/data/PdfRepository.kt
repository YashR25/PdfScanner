package com.example.simplepdfscanner.data

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.util.Log
import com.example.simplepdfscanner.model.PdfModel
import com.example.simplepdfscanner.util.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class PdfRepository @Inject constructor(private val dao: PdfDao, val context: Application){

    suspend fun createPdf(bitmapList: List<Bitmap>){

        withContext(Dispatchers.IO){
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
        val pdf = PdfModel(uri = uri, pdfName = pathName, pdfDesc = currentDateAndTime)
            dao.insertPdf(pdf)
            Log.d("Repository",uri)
        }
    }

     suspend fun getAllPdf(): Flow<List<PdfModel>> {
         val list: Flow<kotlin.collections.List<PdfModel>>
         withContext(Dispatchers.IO){
             list = dao.getPdfList()
         }
         return list
    }


}