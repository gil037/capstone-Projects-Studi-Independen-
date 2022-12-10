package com.example.farmersapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.farmersapp.databinding.ItemBinding
import kotlinx.android.synthetic.main.activity_tambah_barang.view.*
import kotlinx.android.synthetic.main.item.view.*

class AdapterBarang : RecyclerView.Adapter<AdapterBarang.HolderBarang>, Filterable {
    private val context: Context
    var barangArrayList: ArrayList<ModelBarang>
    private lateinit var binding: ItemBinding
    private var filterList: ArrayList<ModelBarang>
    private var filter: FilterBarang? = null

    constructor(context: Context, barangArrayList: ArrayList<ModelBarang>) {
        this.context = context
        this.barangArrayList = barangArrayList
        this.filterList = barangArrayList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderBarang {
        binding = ItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return HolderBarang(binding.root)
    }

    override fun onBindViewHolder(holder: HolderBarang, position: Int) {
        val model = barangArrayList[position]

        holder.namaItem.text = model.Nama
        holder.hargaItem.text = model.Harga
        Glide.with(context).load(model.Url).into(holder.imageBarang)
    }

    override fun getItemCount(): Int {
        return barangArrayList.size
    }

    inner class HolderBarang(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaItem = binding.tvNamaBarang
        val hargaItem = binding.tvHargaBarang
        val imageBarang = binding.ivBarang

    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = FilterBarang(filterList, this)
        }
        return filter as FilterBarang
    }
}