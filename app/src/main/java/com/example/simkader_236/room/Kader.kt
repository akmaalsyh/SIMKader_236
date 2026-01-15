package com.example.simkader_236.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "kader_table")
data class Kader(
    @PrimaryKey(autoGenerate = true)
    val id_kader: Int = 0,
    val nim: String,
    val nama: String,
    val prodi: String,
    val angkatan: Int,
    val status: String
)