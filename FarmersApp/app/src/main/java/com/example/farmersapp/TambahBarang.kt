package com.example.farmersapp

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import coil.load
import coil.transform.CircleCropTransformation
import com.example.farmersapp.databinding.ActivityHalamanUtamaBinding
import com.example.farmersapp.databinding.ActivityTambahBarangBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class TambahBarang : AppCompatActivity() {

    private lateinit var binding: ActivityTambahBarangBinding
    private val CAMERA_REQUEST_CODE = 1
    private val GALLERY_REQUEST_CODE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnCamera.setOnClickListener {
          cameraPermision()
        }
        binding.btnGaleri.setOnClickListener {
          galeriPermission()
        }
        binding.circleImageView.setOnClickListener {
            val pictureDialog = AlertDialog.Builder(this)
            pictureDialog.setTitle("Select Action")
            val pictureDialogItem = arrayOf("Select photo from Gallery",
                "Capture photo from Camera")
            pictureDialog.setItems(pictureDialogItem) { dialog, which ->

                when (which) {
                    0 -> galeri()
                    1 -> camera()
                }
            }

            pictureDialog.show()
        }

        }

    private fun galeriPermission() {
      Dexter.withContext(this).withPermission(
          android.Manifest.permission.READ_EXTERNAL_STORAGE
      ).withListener(object :PermissionListener{
          override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
            galeri()
          }

          override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
              Toast.makeText(this@TambahBarang,"No Storage Permission",Toast.LENGTH_SHORT).show()
              showRotationalDialogForPermission()
          }

          override fun onPermissionRationaleShouldBeShown(
              p0: PermissionRequest?,
              p1: PermissionToken?,
          ) {
              showRotationalDialogForPermission()
          }

      }).onSameThread().check()
    }

    private fun galeri() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun cameraPermision() {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA).withListener(

                object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        report?.let {

                            if (report.areAllPermissionsGranted()) {
                                camera()
                            }

                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?) {
                        showRotationalDialogForPermission()
                    }

                }
            ).onSameThread().check()
    }

    private fun showRotationalDialogForPermission() {
        AlertDialog.Builder(this)
            .setMessage("It looks like you have turned off permissions"
                    + "required for this feature. It can be enable under App settings!!!")

            .setPositiveButton("Go TO SETTINGS") { _, _ ->

                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)

                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }

            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


    private fun camera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_REQUEST_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {

                CAMERA_REQUEST_CODE -> {

                    val bitmap = data?.extras?.get("data") as Bitmap


                    binding.circleImageView.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                }

                GALLERY_REQUEST_CODE -> {

                    binding.circleImageView.load(data?.data) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }

                }
            }

        }

    }
}

