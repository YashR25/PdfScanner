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
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.simplepdfscanner.ImageCropActivity.Companion.RESULT_IMAGE
import com.example.simplepdfscanner.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        val onDestinationlistener = NavController.OnDestinationChangedListener { controller, destination, arguments ->

            if (destination.id == R.id.createPdfFragment ){
                hideBottomBar()
                binding.toolbar.title = "Scan Documents"
                navController.clearBackStack(R.id.createPdfFragment)
            }else if (destination.id == R.id.homeFragment){
                showBottomBar()
                binding.toolbar.title = "Your Documents"
                navController.clearBackStack(R.id.homeFragment)
                binding.startScan.setOnClickListener {
                    navController.navigate(R.id.action_homeFragment_to_createPdfFragment)
                }
            }else if (destination.id == R.id.settingsFragment){
                showBottomBar()
                binding.toolbar.title = "Settings"
                navController.clearBackStack(R.id.settingsFragment)
                binding.startScan.setOnClickListener {
                    navController.navigate(R.id.action_settingsFragment_to_createPdfFragment)
                }

            }
        }

        navController.addOnDestinationChangedListener(onDestinationlistener)
    }

    private fun hideBottomBar(){
        binding.bottomAppBar.visibility = GONE
        binding.startScan.hide()
    }

    private fun showBottomBar(){
        binding.bottomAppBar.visibility = VISIBLE
        binding.startScan.show()
        binding.bottomNavigationView.background = null
    }

}