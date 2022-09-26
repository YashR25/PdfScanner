package com.example.simplepdfscanner.di

import android.app.Application
import androidx.room.Room
import com.example.simplepdfscanner.data.PdfDao
import com.example.simplepdfscanner.data.PdfDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PdfModule {

    @Singleton
    @Provides
    fun getPdfDatabase(application: Application): PdfDatabase{
            return Room.databaseBuilder(application,
                PdfDatabase::class.java,
                "pdf_database")
                .build()
    }

    @Singleton
    @Provides
    fun getPdfDao(database: PdfDatabase):PdfDao = database.pdfDao()
}