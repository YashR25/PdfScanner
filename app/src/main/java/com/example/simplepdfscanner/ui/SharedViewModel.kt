package com.example.simplepdfscanner.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SharedViewModel: ViewModel() {

    private val _imageList: MutableLiveData<List<Bitmap>> = MutableLiveData()
    val imageList: LiveData<List<Bitmap>>
        get() = _imageList

    private val mutableImageList = mutableListOf<Bitmap>()

    init {
        Log.d("ViewModel","ViewModel Called")
    }

    fun addImage(image: Bitmap?) {
        if (image != null) {
            mutableImageList.add(image)
            _imageList.value = mutableImageList
        }

    }

    fun getUri(data: Uri?, baseContext: Context): String? {
        val filePathColumn = arrayOf( MediaStore.Images.Media.DATA)
        val cursor = baseContext.contentResolver.query(data!!, filePathColumn,null,null,null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = columnIndex?.let { cursor.getString(it) }
        val file = filePath?.let { File(it) }
        val fileName = file?.name
        cursor?.close()
        return filePath
    }

}