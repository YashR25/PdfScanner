package com.example.simplepdfscanner

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.simplepdfscanner.ImageCropActivity.Companion.RESULT_IMAGE
import com.example.simplepdfscanner.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var pickerresultLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var cropImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding
    private var photoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){

        }
        cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d("MainActivity","called")
            if (it.resultCode == RESULT_OK){
                val byteArray = it.data?.getByteArrayExtra(RESULT_IMAGE)
                val image = BitmapFactory.decodeByteArray(byteArray,0,byteArray!!.size)
                Log.d("MainActivity",image.toString())
            }
        }
        pickerresultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d("MainActivity", "Called Picker")
            if (it.resultCode == RESULT_OK){
                cropImageLauncher.launch(ImageCropActivity.newIntent(this,it.data?.data.toString()))
            }

        }
        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d("MainActivity", photoPath.toString())
            if (it.resultCode == RESULT_OK){
                cropImageLauncher.launch(photoPath?.let { it1 -> ImageCropActivity.newIntent(this, it1) })
            }
        }
        askPermission()
        setView()
    }

    private fun askPermission(){
        if(ContextCompat.checkSelfPermission(
                this,Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!= PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,Manifest.permission.CAMERA
            ) != PERMISSION_GRANTED){
            permissionLauncher.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun setView() {
        binding.btnPick.setOnClickListener{
            Log.d("MainActivity", "Clicked")
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("MaliY")
            builder.setMessage("Where would you like to choose the image?")
            builder.setPositiveButton("Gallery") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                pickerresultLauncher.launch(intent)

            }
            builder.setNegativeButton("Camera") { dialog, _ ->
                dialog.dismiss()
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    var photoFile: File? = null
                    try {
                        photoFile = createImageFile()
                    } catch (ex: IOException) {
                        Log.i("Main", "IOException")
                    }
                    if (photoFile != null) {
                        val builder1 = StrictMode.VmPolicy.Builder()
                        StrictMode.setVmPolicy(builder1.build())
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                        cameraResultLauncher.launch(cameraIntent)
                    }
                }
            }
            builder.setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
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
        photoPath = "file:" + image.absolutePath
        return image
    }
}