package com.example.ugd3_kelompok19.room

import androidx.room.*
@Dao

interface PeminjamDao {
    @Insert
    suspend fun addPeminjam(peminjam: Peminjam)

    @Update
    suspend fun updatePeminjam(peminjam: Peminjam)

    @Delete
    suspend fun deletePeminjam(peminjam: Peminjam)

    @Query("SELECT * FROM peminjam")
    suspend fun getPeminjams(): List<Peminjam>

    @Query("SELECT * FROM peminjam WHERE id =:id_peminjam")
    suspend fun getPeminjam(id_peminjam: Int): List<Peminjam>
}