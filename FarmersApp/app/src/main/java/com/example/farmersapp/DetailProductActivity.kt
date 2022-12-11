package com.example.farmersapp

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
    private var img = ""
    private var hargabar = ""
    private var namabar = ""
    private var deskripsibar = ""
    private lateinit var auth:FirebaseAuth
    private var isAdd=false
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        binding=ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()

        barangId = intent.getStringExtra("barangId")!!
         namabar = intent.getStringExtra("nama")!!
         deskripsibar = intent.getStringExtra("deskripsi")!!
         hargabar = intent.getStringExtra("harga")!!
         img = intent.getStringExtra("img")!!

        tv1.text = namabar
        tv_description.text = deskripsibar
        tv_harga.text = hargabar
        Glide.with(this).load(img).into(iv_product)



binding.btnKeranjang.setOnClickListener {
    val builder=AlertDialog.Builder(this@DetailProductActivity)
    if (auth.currentUser==null){
        Toast.makeText(this,"not Login",Toast.LENGTH_SHORT).show()
    }else{
        if (isAdd){

        }else{

            addBarang()
            val intent=Intent(this,BuktiActivity::class.java)
            startActivity(intent)
        }
    }
}
    }
    private fun addBarang() {
        Log.d(TAG,"addBarang:Pembelian berhasil")
        val time = System.currentTimeMillis()
        val hashMap = HashMap<String, Any>()
        hashMap["barangId"]=barangId
        hashMap["time"]=time
        hashMap["nama"]=namabar
        hashMap["deskripsi"]=deskripsibar
        hashMap["harga"]=hargabar
        val ref= FirebaseDatabase.getInstance().getReference("UserLogin")
            ref.child(auth.uid!!).child("Pesan")
                .setValue(hashMap)
                .addOnSuccessListener {
                Log.d(TAG,"addBarang:Add Berhasil")

                }.addOnFailureListener{
                    Log.d(TAG,"addBarang:Gagalchild ${it.message}")
                }
    }
    private fun checkAdd(){
        val ref=FirebaseDatabase.getInstance().getReference("UserLogin")
        ref.child(auth.uid!!).child("Pesan").child("bookId")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    isAdd=snapshot.exists()
                    if (isAdd){

                    }else{
                        binding.btnKeranjang.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_add_24,0,0)
                        binding.btnKeranjang.text="Masukan Keranjang"
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

}

