package com.example.simplepdfscanner.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.simplepdfscanner.model.PdfModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PdfDao {

    @Query("SELECT * FROM pdf_table")
    fun getPdfList():Flow<List<PdfModel>>

    @Insert
    fun insertPdf(pdfModel: PdfModel)

    @Update
    fun updatePdf(pdfModel: PdfModel)

    @Delete
    fun deletePdf(pdfModel: PdfModel)
}