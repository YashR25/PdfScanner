package com.example.simplepdfscanner.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Environment
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

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents

        return image
    }

    fun addImage(image: Bitmap?) {
        if (image != null) {
            mutableImageList.add(image)
            _imageList.value = mutableImageList
        }

    }

}