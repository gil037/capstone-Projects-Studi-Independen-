package com.example.farmersapp

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import com.example.farmersapp.databinding.ActivityHalamanUtamaBinding
import com.example.farmersapp.databinding.ActivityTambahBarangBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class TambahBarang : AppCompatActivity() {

    private lateinit var binding: ActivityTambahBarangBinding
    private lateinit var imgUri:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.circleImageView.isEnabled=true

      if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
          ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),100)
      }else{
        binding.circleImageView.isEnabled=true
      }
        binding.circleImageView.setOnClickListener {
            val i=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,101)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==101){
            var picture:Bitmap?= data?.getParcelableExtra<Bitmap>("data")
            binding.circleImageView.setImageBitmap(picture)
            uploadImg()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode==100 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            binding.circleImageView.isEnabled=true

        }
    }

    private fun uploadImg() {
    val baos= ByteArrayOutputStream()
    val ref=FirebaseStorage.getInstance().reference.child("img_barang/${FirebaseAuth.getInstance().currentUser!!.uid}")

        val img=baos.toByteArray()
        ref.putBytes(img).addOnCompleteListener{
            if (it.isSuccessful){
                ref.downloadUrl.addOnCompleteListener{
                    it.result.let { uri->
                        imgUri=uri
                    }
                }
            }
        }
    }
}