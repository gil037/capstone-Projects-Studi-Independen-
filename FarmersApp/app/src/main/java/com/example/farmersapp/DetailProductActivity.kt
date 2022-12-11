package com.example.farmersapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import coil.load
import com.bumptech.glide.Glide
import com.example.farmersapp.databinding.ActivityDetailProductBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_tambah_barang.*

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private var barangId = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        binding=ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        barangId = intent.getStringExtra("barangId")!!
        val namabar = intent.getStringExtra("nama")
        val deskripsibar = intent.getStringExtra("deskripsi")
        val hargabar = intent.getStringExtra("harga")
        val img = intent.getStringExtra("img")

        tv1.text = namabar
        tv_description.text = deskripsibar
        tv_harga.text = hargabar
        Glide.with(this).load(img).into(iv_product)



        binding.btnPesan.setOnClickListener {
        AlertDialog.Builder(this).apply {
            setTitle("Pembelian")
            setMessage("Anda yakin ingin pesan barang?")
            setPositiveButton("Yakin"){_,_->}
            setNegativeButton("Tidak"){_,_->}
        }

        }

    }
}
