package com.example.farmersapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmersapp.databinding.ActivityBuktiBinding
import com.example.farmersapp.databinding.ActivityHalamanUtamaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BuktiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuktiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBuktiBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}