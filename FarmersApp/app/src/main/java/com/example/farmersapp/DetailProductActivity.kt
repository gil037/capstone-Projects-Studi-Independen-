package com.example.farmersapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import coil.load
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_detail_product.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_tambah_barang.*

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding:DetailProductActivity
    private var barangId=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)

       loadBarang()
    }

    private fun loadBarangDetail() {
        val ref=FirebaseDatabase.getInstance().getReference("Data Barang")
        ref.child(barangId).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val time="${snapshot.child("time").value}"
               val Deskripsi ="${ snapshot.child("Deskripsi").value}"
               val Nama = "${snapshot.child("Nama").value}"
               val Harga = "${snapshot.child("Harga").value}"
               val Url = "${snapshot.child("Url").value}"



            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun loadBarang(){
        barangId=intent.getStringExtra("barangId")!!
        val namabar=intent.getStringExtra("nama")
        val deskripsibar=intent.getStringExtra("deskripsi")
        val hargabar=intent.getStringExtra("harga")
        val img=intent.getStringExtra("img")

        tv_name.text=namabar
        tv_description.text=deskripsibar
        tv_harga.text=hargabar
        Glide.with(this).load(img).into(iv_product)


    }
}