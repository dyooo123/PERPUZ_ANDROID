package com.example.ugd3_kelompok19.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val username: String,
    val email: String,
    val password: String,
    val tanggalLahir: String,
    val noTelp: String
)