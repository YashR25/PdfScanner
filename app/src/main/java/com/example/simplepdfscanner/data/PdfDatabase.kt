package com.example.simplepdfscanner.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.simplepdfscanner.model.PdfModel

@Database(entities = [PdfModel::class], version = 1, exportSchema = false)
abstract class PdfDatabase:RoomDatabase() {

    abstract fun pdfDao(): PdfDao

    companion object{
        @Volatile
        private var INSTANCE: PdfDatabase? = null
        fun getDatabase(context: Context): PdfDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context,PdfDatabase::class.java,"pdf_database").build()
                INSTANCE = instance
                instance
            }
        }
    }

}