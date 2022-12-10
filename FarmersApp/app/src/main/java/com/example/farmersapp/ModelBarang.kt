package com.example.farmersapp

class ModelBarang {
var uid:String=""
var Nama:String=""
var Harga:String=""
var Deskripsi:String=""
var Url:String=""
var time:String=""

    constructor()
    constructor(
        uid: String,
        Nama: String,
        Harga: String,
        Deskripsi: String,
        Url: String,
        time: String,
    ) {
        this.uid = uid
        this.Nama = Nama
        this.Harga = Harga
        this.Deskripsi = Deskripsi
        this.time = time
        this.Url=Url
    }

}