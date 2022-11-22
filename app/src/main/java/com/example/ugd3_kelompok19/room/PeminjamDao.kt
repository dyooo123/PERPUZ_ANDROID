//package com.example.ugd3_kelompok19.room
//
//import androidx.room.*
//import com.example.ugd3_kelompok19.models.Peminjam
//
//@Dao
//
//interface PeminjamDao {
//    @Insert
//    suspend fun addPeminjam(peminjam: Peminjam)
//
//    @Update
//    suspend fun updatePeminjam(peminjam: PeminjamRoom)
//
//    @Delete
//    suspend fun deletePeminjam(peminjam: PeminjamRoom)
//
//    @Query("SELECT * FROM peminjam")
//    suspend fun getPeminjams(): List<PeminjamRoom>
//
//    @Query("SELECT * FROM peminjam WHERE id =:id_peminjam")
//    suspend fun getPeminjam(id_peminjam: Int): List<PeminjamRoom>
//}