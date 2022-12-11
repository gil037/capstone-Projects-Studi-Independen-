package com.example.farmersapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.example.farmersapp.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.view.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth:FirebaseAuth
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
    auth=FirebaseAuth.getInstance()
        loadUser()
    binding.ivProfile.setOnClickListener{

    }
        binding.buttonEdit.setOnClickListener {
            val intent=Intent(this,UpdateProfileActivity::class.java)
            startActivity(intent)
        }
    }
    private fun loadUser(){
        val ref=FirebaseDatabase.getInstance().getReference("UserLogin")
        ref.child(auth.uid!!).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               val email="${snapshot.child("Email").value}"
               val nama="${snapshot.child("Nama").value}"
               val avatar="${snapshot.child("Avatar").value}"
               val uid="${snapshot.child("Id").value}"
               val phone="${snapshot.child("Phone").value}"
               val userType="${snapshot.child("UserType").value}"

                binding.tvName.text=nama
                binding.tvEmail.text=email
                binding.tvHp.text=phone
                try {
                    Glide.with(this@ProfileActivity)
                        .load(avatar)
                        .into(binding.ivProfile)
                }catch (e:Exception){

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.btnOut.setOnClickListener {
            auth.signOut()
            Intent(this@ProfileActivity,LoginActivity::class.java).also {
                it.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

}