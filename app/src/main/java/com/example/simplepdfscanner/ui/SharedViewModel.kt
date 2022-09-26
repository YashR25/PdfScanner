package com.example.simplepdfscanner.ui

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplepdfscanner.data.PdfRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(val repository: PdfRepository) : ViewModel() {

    private val _imageList: MutableLiveData<List<Bitmap>> = MutableLiveData()
    val imageList: LiveData<List<Bitmap>>
        get() = _imageList

    private val _status:MutableLiveData<Status> = MutableLiveData()
    val status: LiveData<Status>
        get() = _status

    var fileProvider:Uri? = null

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

    fun createPdf(){
        viewModelScope.launch {
            _status.value = Status.isLoading()
            imageList.value?.let { repository.createPdf(it) }
            _status.value = Status.Success()
        }
    }

//    fun getUri(data: Uri?, baseContext: Context): String? {
//        val filePathColumn = arrayOf( MediaStore.Images.Media.DATA)
//        val cursor = baseContext.contentResolver.query(data!!, filePathColumn,null,null,null)
//        cursor?.moveToFirst()
//        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
//        val filePath = columnIndex?.let { cursor.getString(it) }
//        val file = filePath?.let { File(it) }
//        val fileName = file?.name
//        cursor?.close()
//        return filePath
//    }

}

sealed class Status{
    data class isLoading(val message: String = "Loading") :Status()
    data class Success(val message: String = "Successfully Created PDF...") :Status()
}