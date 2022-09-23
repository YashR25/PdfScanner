package com.example.simplepdfscanner

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.simplepdfscanner.databinding.ActivityImageCropBinding
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream


class ImageCropActivity : AppCompatActivity() {

    lateinit var binding: ActivityImageCropBinding

    companion object {
        const val RESULT_IMAGE = "result_image"
        private const val FILE_DIR = "FileDir"
        fun newIntent(context: Context, selectedFilePath: String) =
            Intent(context, ImageCropActivity::class.java).putExtra(FILE_DIR, selectedFilePath)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageCropBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bitmap = assetToBitmap(intent.extras?.getString(FILE_DIR)!!)
        binding.documentScanner.setOnLoadListener {
            binding.progressBar.isVisible = it
        }
        binding.documentScanner.setImage(bitmap)
        binding.btnImageCrop.setOnClickListener{
            lifecycleScope.launch{
                binding.progressBar.isVisible = true
                val image = binding.documentScanner.getCroppedImage()
                binding.progressBar.isVisible = false
//                binding.resultImage.isVisible = true
//                binding.resultImage.setImageBitmap(image)
                val resultIntent = Intent()
                resultIntent.putExtra(RESULT_IMAGE,getByteArray(bitmap))
                setResult(RESULT_OK,resultIntent)
                finish()
            }
        }
    }
    private fun assetToBitmap(file: String): Bitmap =
        contentResolver.openInputStream(Uri.parse(file)).run {
            BitmapFactory.decodeStream(this)
        }

    private fun getByteArray(bitmap: Bitmap): ByteArray{
        var byteArray: ByteArray = byteArrayOf()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            byteArray = stream.toByteArray()
        return byteArray
    }

}