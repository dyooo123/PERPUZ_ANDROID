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

   // @Query("SELECT * FROM peminjam")
    //suspend fun getPeminjam(peminjamId: Int): List<Peminjam>

   // @Query("SELECT * FROM peminjam WHERE id =:peminjam_id")
   // suspend fun getPeminjamById(peminjam_id: Int): List<Peminjam>
}