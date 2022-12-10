package com.example.farmersapp


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.example.farmersapp.databinding.ActivityTambahBarangBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class TambahBarang : AppCompatActivity() {
    private var imgUri: Uri? = null
    private lateinit var binding: ActivityTambahBarangBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
    auth=FirebaseAuth.getInstance()

        binding.btnUpload.setOnClickListener {
            validateData()
        }
        binding.circleImageView.setOnClickListener{
            showImage()
        }
    }



    private var nama=""
    private var harga=""
    private var deskripsi=""
    private var avatar=""
    private fun validateData() {
        nama=binding.namaBarang.text.toString().trim()
        harga=binding.hargaBarang.text.toString().trim()
        deskripsi=binding.deskBarang.text.toString().trim()

        if (nama.isEmpty()){
            binding.namaBarang.error=getString(R.string.nama_barang)
            binding.namaBarang.requestFocus()
        }
        if (harga.isEmpty()){
            binding.hargaBarang.error=getString(R.string.nama_barang)
            binding.hargaBarang.requestFocus()
        }
        if (deskripsi.isEmpty()){
            binding.deskBarang.error= getString(R.string.nama_barang)
            binding.deskBarang.requestFocus()
        }else if (imgUri==null){

        }
        else {
                addDatatostoge()
        }
    }

    private fun addDatatostoge() {
        val time=System.currentTimeMillis()
        val filePath = "Image Barang/$time"
        val  image=FirebaseStorage.getInstance().getReference(filePath)
        image.putFile(imgUri!!).addOnSuccessListener {snapshot->
            val uriTask:Task<Uri> =snapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val uploadImg= "${uriTask.result}"

            uploadData(uploadImg,time)
        }.addOnFailureListener{

        }

    }

    private fun uploadData(uploadImg: String, time: Long) {
        val curent=auth.currentUser!!.uid
        val hashMap= HashMap<String,Any>()
        hashMap["uid"]=curent
        hashMap["Nama"]="$nama"
        hashMap["Harga"]="$harga"
        hashMap["Deskripsi"]="$deskripsi"
        hashMap["Url"]="$uploadImg"
        hashMap["time"]="$time"
        val ref=FirebaseDatabase.getInstance().getReference("Data Barang")
        ref.child("$time").setValue(hashMap).addOnSuccessListener {
            Toast.makeText(this,"Berhasil Tambah Data",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {

        }
    }


    private fun showImage() {
        val popmenu = PopupMenu(this, binding.circleImageView)
        popmenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popmenu.menu.add(Menu.NONE, 1, 1, "Galery")
        popmenu.show()
        popmenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == 0) {
                pickCamera()
            } else if (id == 1) {
                pickGalery()
            }
            true
        }
    }

    private fun pickCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Camera")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Camera description")

        imgUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri)
        cameraActivity.launch(intent)
    }

    private fun pickGalery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galeryActivity.launch(intent)
    }
    private val galeryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                imgUri = data!!.data

                binding.circleImageView.setImageURI(imgUri)
            } else {
                Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show()
            }
        }
    )
    private val cameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                // imgUri=data!!.data

                binding.circleImageView.setImageURI(imgUri)
            } else {
                Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show()
            }
        }
    )
}

