package com.example.farmersapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.media.MediaScannerConnection
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
import com.bumptech.glide.Glide
import com.example.farmersapp.databinding.ActivityUpdateProfileBinding
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_profile.*

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var auth: FirebaseAuth
    private var imgUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Mohon tunggu")
        progressDialog.setCanceledOnTouchOutside(false)
        auth = FirebaseAuth.getInstance()
        loadUser()
        binding.edtImage.setOnClickListener {
            showImage()
        }

        binding.btnUpdate.setOnClickListener {
            validateData()
        }
    }

    private var nama = ""
    private fun validateData() {
        nama = binding.edtName.text.toString().trim()
        if (nama.isEmpty()) {
            binding.edtName.error = getString(R.string.errorusername)
            binding.edtName.requestFocus()
        } else {
            if (imgUri == null) {
                updateProfile("")
            } else {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        progressDialog.setMessage("Upload image")
        progressDialog.show()

        val filePath = "Profile/" + auth.uid

        val reff = FirebaseStorage.getInstance().getReference(filePath)
        reff.putFile(imgUri!!).addOnSuccessListener {snapshot->
            val uriTask:Task<Uri> =snapshot.storage.downloadUrl
            while (!uriTask.isSuccessful);
            val uploadImg= "${uriTask.result}"

            updateProfile(uploadImg)

        }.addOnFailureListener {
            progressDialog.dismiss()
            Toast.makeText(this, "Gagal Upload Image ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProfile(uploadImgurl: String) {
    progressDialog.setMessage("Update Profile...")
        progressDialog.show()
        val hashMap: HashMap<String,Any> = HashMap()
        hashMap["Nama"]="$nama"
        if (imgUri!=null){
         hashMap["Avatar"]=uploadImgurl
        }
        val ref=FirebaseDatabase.getInstance().getReference("UserLogin")
        ref.child(auth.uid!!).updateChildren(hashMap).addOnSuccessListener {
            progressDialog.dismiss()
            Toast.makeText(this, "Success update", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            progressDialog.dismiss()
            Toast.makeText(this, "Gagal Upload Image ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadUser() {
        val ref = FirebaseDatabase.getInstance().getReference("UserLogin")
        ref.child(auth.uid!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val nama = "${snapshot.child("Nama").value}"
                val avatar = "${snapshot.child("Nama").value}"
                binding.edtName.setText(nama)

                try {
                    Glide.with(this@UpdateProfileActivity)
                        .load(avatar)
                        .into(binding.edtImage)
                } catch (e: Exception) {

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun showImage() {
        val popmenu = PopupMenu(this, binding.edtImage)
        popmenu.menu.add(Menu.NONE, 1, 1, "Galery")
        popmenu.show()

        popmenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
           if (id == 1) {
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

    private val cameraActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                // imgUri=data!!.data

                binding.edtImage.setImageURI(imgUri)
            } else {
                Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show()
            }
        }
    )
    private val galeryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imgUri = data!!.data

                binding.edtImage.setImageURI(imgUri)
            } else {
                Toast.makeText(this, "Batal", Toast.LENGTH_SHORT).show()
            }
        }
    )
}