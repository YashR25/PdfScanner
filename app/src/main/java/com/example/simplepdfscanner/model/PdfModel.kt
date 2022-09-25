package com.example.simplepdfscanner.model

data class PdfModel(
    val id: Long,
    val uri:String?,
    val pdfName: String,
    val pdfDesc: String
)