package com.example.simplepdfscanner.ui.createPdfFragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.Intent.ACTION_PICK
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import com.example.simplepdfscanner.ImageCropActivity
import com.example.simplepdfscanner.adapter.ViewPagerAdapter
import com.example.simplepdfscanner.databinding.FragmentCreatePdfBinding
import com.example.simplepdfscanner.ui.SharedViewModel
import com.example.simplepdfscanner.util.FileUtil
import java.io.File
import java.io.IOException


class CreatePdfFragment : Fragment() {


    lateinit var binding: FragmentCreatePdfBinding
    lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    lateinit var cameraLauncher:ActivityResultLauncher<Intent>
    lateinit var permissionLauncher:ActivityResultLauncher<Array<String>>
    lateinit var cropImageLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCreatePdfBinding.inflate(inflater)
        val viewModel:SharedViewModel by viewModels()

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){}

        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                cropImageLauncher.launch(ImageCropActivity.newIntent(activity?.baseContext!!,it.data?.data.toString()))
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                cropImageLauncher.launch(FileUtil.getFilePath()
                    ?.let { it1 -> ImageCropActivity.newIntent(activity?.baseContext!!, it1) })
            }
        }

        cropImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK){
                val byteArray = it.data?.getByteArrayExtra(ImageCropActivity.RESULT_IMAGE)
                viewModel.addImage(FileUtil.byteArrayToBitmap(byteArray))
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
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,FileUtil.getFileProvider(requireContext()))
                        cameraLauncher.launch(cameraIntent)
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
        askPermission()
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