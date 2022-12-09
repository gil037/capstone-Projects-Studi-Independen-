package com.example.farmersapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.farmersapp.databinding.ActivityHalamanUtamaBinding

class halaman_utama : AppCompatActivity() {
    private lateinit var binding:ActivityHalamanUtamaBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHalamanUtamaBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}