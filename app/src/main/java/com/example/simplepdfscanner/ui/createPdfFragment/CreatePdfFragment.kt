package com.example.simplepdfscanner.ui.createPdfFragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.drm.DrmStore.Action
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.simplepdfscanner.ImageCropActivity
import com.example.simplepdfscanner.R
import com.example.simplepdfscanner.adapter.ViewPagerAdapter
import com.example.simplepdfscanner.databinding.FragmentCreatePdfBinding
import com.example.simplepdfscanner.ui.SharedViewModel
import java.io.File
import java.io.IOException


class CreatePdfFragment : Fragment() {


    lateinit var binding: FragmentCreatePdfBinding
    lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var cameraLauncher:ActivityResultLauncher<Intent>
    lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    lateinit var cropImageLauncher: ActivityResultLauncher<Intent>
    var photoPath:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatePdfBinding.inflate(inflater)
        val viewModel = ViewModelProvider(this)[SharedViewModel::class.java]
        askPermission()
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                cropImageLauncher.launch(ImageCropActivity.newIntent(activity?.baseContext!!,it.data?.data.toString()))
            }
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                cropImageLauncher.launch(ImageCropActivity.newIntent(activity?.baseContext!!,photoPath))
            }
        }
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){}

        cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val byteArray = it.data?.getByteArrayExtra(ImageCropActivity.RESULT_IMAGE)
                val image = BitmapFactory.decodeByteArray(byteArray,0,byteArray!!.size)
                viewModel.addImage(image)
            }
        }
        binding.btnPickImage.setOnClickListener {
            val builder = AlertDialog.Builder(activity).apply {
                setTitle("PdfScanner")
                setMessage("Where wold you like to get an image from?")
                setPositiveButton("Gallery"){dialog,_->
                    dialog.dismiss()
                    val intent = Intent(ACTION_PICK)
                    intent.type = "image/*"
                    galleryLauncher.launch(intent)
                }
                setNegativeButton("Camera"){dialog,_->
                    dialog.dismiss()
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    if(cameraIntent.resolveActivity(activity?.packageManager!!) != null){
                        var photoFile: File? = null
                        try {
                            photoFile = viewModel.createImageFile()
                            photoPath = "file:" + photoFile.absolutePath
                        }catch (ex: IOException) {
                            Log.i("Main", "IOException")
                        }
                        if (photoFile != null) {
                            val builder1 = StrictMode.VmPolicy.Builder()
                            StrictMode.setVmPolicy(builder1.build())
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                            cameraLauncher.launch(cameraIntent)
                        }
                    }
                }
                setNeutralButton("Cancel"){dialog,_->
                    dialog.dismiss()
                }
            }
            val dialog = builder.create()
            dialog.show()
        }
        viewModel.imageList.observe(viewLifecycleOwner){
            binding.viewPager.adapter = ViewPagerAdapter(it)
        }
        return binding.root
    }
    private fun askPermission(){
        if(ContextCompat.checkSelfPermission(
                activity?.baseContext!!, Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                activity?.baseContext!!, Manifest.permission.WRITE_EXTERNAL_STORAGE
            )!= PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                activity?.baseContext!!, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ){
            permissionLauncher.launch(arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ))
        }
    }
}