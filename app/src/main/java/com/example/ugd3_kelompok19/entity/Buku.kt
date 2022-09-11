package com.example.ugd3_kelompok19.entity

class Buku (var buku: String, var kategori: String) {

    companion object {
        @JvmField
        var listOfBooks = arrayOf(
            Buku("Maling Kundang","Dongeng"),
            Buku("Sejarah Dunia Lengkap","Sejarah"),
            Buku("Hyperabad","Novel"),
            Buku("Doraemon","Komik"),
            Buku("Ensiklopedia Sains","Ensiklopedia"),
            Buku("Bobo","Majalah"),
            Buku("Mohammad Hatta","Biografi"),
            Buku("Kamus Inggris Indonesia","Kamus"),
            Buku("Menulis Karya Ilmiah","Karya Ilmiah"),
            Buku("Fotografi Poetrets","Fotografi")
        )
    }
}