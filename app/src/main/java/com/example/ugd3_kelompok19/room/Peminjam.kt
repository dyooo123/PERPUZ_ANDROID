package com.example.ugd3_kelompok19.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Peminjam (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nama: String,
    val alamat: String,
    val judulBukuPinjaman: String,
    val tanggalPinjam: String,
    val tanggalKembali: String
)