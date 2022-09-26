package com.example.simplepdfscanner.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pdf_table")
data class PdfModel(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uri:String?,
    val pdfName: String,
    val pdfDesc: String
)