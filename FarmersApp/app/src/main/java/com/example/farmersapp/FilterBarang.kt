package com.example.farmersapp

import android.widget.Filter

class FilterBarang:Filter {
    private var filterList:ArrayList<ModelBarang>

    private var adapterBarang:AdapterBarang

    constructor(filterList: ArrayList<ModelBarang>, adapterBarang: AdapterBarang) : super() {
        this.filterList = filterList
        this.adapterBarang = adapterBarang
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint=constraint
        val results=FilterResults()
        if (constraint!=null && constraint.isEmpty()){
            constraint=constraint.toString()
            val filterModel:ArrayList<ModelBarang> = ArrayList()
            for (i in 0 until filterList.size){
                if (filterList[i].Nama.contains(constraint)) {
                    filterModel.add(filterList[i])
                }

            }
            results.count=filterModel.size
            results.values=filterModel
        }
        else{
            results.count=filterList.size
            results.values=filterList
        }
return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults) {
        adapterBarang.barangArrayList = results.values as ArrayList<ModelBarang>

        adapterBarang.notifyDataSetChanged()
    }
}