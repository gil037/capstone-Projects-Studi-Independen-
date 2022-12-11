package com.example.farmersapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.farmersapp.databinding.BuktiBinding
import com.example.farmersapp.databinding.ItemBinding

class AdapterBukti:RecyclerView.Adapter<AdapterBukti.HolderBukti> {
    private val context: Context
    var barangArrayList: ArrayList<ModelBarang>
    private lateinit var binding: BuktiBinding

    constructor(context: Context, barangArrayList: ArrayList<ModelBarang>) {
        this.context = context
        this.barangArrayList = barangArrayList
    }
    inner class HolderBukti(itemView:View):RecyclerView.ViewHolder(itemView){
        val namaBar=binding.namaBukti
        val hargaBar=binding.hargaBukti
        val deskripBar=binding.tvDescription
        val imgBar=binding.ivBukti
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBukti {
        binding=BuktiBinding.inflate(LayoutInflater.from(context),parent,false)
        return HolderBukti(binding.root)
    }

    override fun onBindViewHolder(holder: HolderBukti, position: Int) {
        val model = barangArrayList[position]
        val barang=model.time
        val nama=model.Nama
        val deskripsi=model.Deskripsi
        val img=model.Url
        val harga=model.Harga

        holder.namaBar.text=model.time
        holder.hargaBar.text=model.Harga
        holder.deskripBar.text=model.Deskripsi
        Glide.with(context).load(model.Url).into(holder.imgBar)
    }

    override fun getItemCount(): Int {
       return barangArrayList.size
    }
}