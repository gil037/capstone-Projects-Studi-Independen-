package com.example.farmersapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
    private lateinit var binding:ActivityDetailProductBinding
    private var barangId=""
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        binding=ActivityDetailProductBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
    binding.btnPesan.setOnClickListener{
        loadBarang()
    }

    }

    private fun loadBarang(){
        val curent=auth.currentUser!!.uid
        val ref=FirebaseDatabase.getInstance()
        barangId=intent.getStringExtra("barangId")!!
        val namabar=intent.getStringExtra("nama")
        val deskripsibar=intent.getStringExtra("deskripsi")
        val hargabar=intent.getStringExtra("harga")
        val img=intent.getStringExtra("img")

        tv1.text=namabar
        tv_description.text=deskripsibar
        tv_harga.text=hargabar
        Glide.with(this).load(img).into(iv_product)

        }
    private fun getDetail(barangId:String){
        val curent=auth.currentUser!!.uid
            val ref=FirebaseDatabase.getInstance().getReference("Pesan").child(curent).child(barangId)
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val barang=snapshot.getValue(ModelBarang::class.java)
                Glide.with(this@DetailProductActivity).load(barang!!.Url).into(iv_product)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    }
