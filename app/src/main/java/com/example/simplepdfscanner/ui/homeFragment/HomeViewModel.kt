package com.example.simplepdfscanner.ui.homeFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplepdfscanner.data.PdfRepository
import com.example.simplepdfscanner.model.PdfModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: PdfRepository): ViewModel() {
    private val _pdfList:MutableLiveData<List<PdfModel>> = MutableLiveData()
    val pdfList:LiveData<List<PdfModel>>
        get() = _pdfList


    init {
        viewModelScope.launch {
            repository.getAllPdf().collect{
                _pdfList.value = it
            }
        }

    }




}