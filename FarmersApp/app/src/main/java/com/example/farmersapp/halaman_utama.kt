package com.example.farmersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.farmersapp.databinding.ActivityHalamanUtamaBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class halaman_utama : AppCompatActivity() {
    private lateinit var binding:ActivityHalamanUtamaBinding
    private lateinit var barangArraylist:ArrayList<ModelBarang>
    private lateinit var adapterBarang:AdapterBarang
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityHalamanUtamaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadBarang()

        binding.cart.setOnClickListener {
            val intent=Intent(this,ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.searchView.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    adapterBarang.filter.filter(s)
                }
                catch (e:Exception){

                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun loadBarang() {
        barangArraylist= ArrayList()
        val ref=FirebaseDatabase.getInstance().getReference("Data Barang")
        ref.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              barangArraylist.clear()
               for (ds in snapshot.children){
                   val model=ds.getValue(ModelBarang::class.java)

                   barangArraylist.add(model!!)
               }
                adapterBarang=AdapterBarang(this@halaman_utama,barangArraylist)

                binding.itemUserRecycleview.adapter=adapterBarang
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}