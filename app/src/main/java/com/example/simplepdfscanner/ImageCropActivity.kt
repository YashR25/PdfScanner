package com.example.simplepdfscanner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.simplepdfscanner.databinding.ActivityImageCropBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class ImageCropActivity : AppCompatActivity() {

    lateinit var binding: ActivityImageCropBinding

    companion object {
        const val RESULT_IMAGE = "result_image"
        private const val FILE_DIR = "FileDir"
        fun newIntent(context: Context, selectedFilePath: String) =
            Intent(context, ImageCropActivity::class.java).putExtra(FILE_DIR, selectedFilePath)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageCropBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val file = intent.extras?.getString(FILE_DIR)
        val bitmap = file?.let { assetToBitmap(it) }
        binding.documentScanner.setOnLoadListener {
            binding.progressBar.isVisible = it
        }
        if (bitmap != null) {
            binding.documentScanner.setImage(bitmap)
        }
        binding.btnImageCrop.setOnClickListener{
            lifecycleScope.launch{
                binding.progressBar.isVisible = true
                val image = binding.documentScanner.getCroppedImage()
                binding.progressBar.isVisible = false
//                binding.resultImage.isVisible = true
//                binding.resultImage.setImageBitmap(image)
                val byteArray: ByteArray
                withContext(Dispatchers.IO){
                    byteArray = getByteArray(image)
                    Log.d("ImageCropActivity",byteArray.toString())
                }
                val resultIntent = Intent()
                resultIntent.putExtra(RESULT_IMAGE, byteArray)
                setResult(RESULT_OK,resultIntent)
                finish()
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("Recycle")
    private fun assetToBitmap(file: String): Bitmap {
        val inputStream = contentResolver.openInputStream(Uri.parse(file))
        return BitmapFactory.decodeStream(inputStream)
//        val source = ImageDecoder.createSource(this.contentResolver,Uri.parse(file))
//        return ImageDecoder.decodeBitmap(source)
    }


    @SuppressLint("SuspiciousIndentation")
    private fun getByteArray(bitmap: Bitmap): ByteArray{
        val byteArray: ByteArray
        val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream)
            byteArray = stream.toByteArray()
        return byteArray
    }

}