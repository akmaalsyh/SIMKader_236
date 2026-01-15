package com.example.simkader_236.modeldata

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

// Model Utama: Digunakan untuk MySQL (Serializable) dan Room (Entity)
@Serializable
@Entity(tableName = "kader") // Table sesuai rancangan database
data class DataKader(
    @PrimaryKey(autoGenerate = true)
    val id_kader: Int = 0,    // Primary Key [cite: 385]
    val nim: String,          // Unique Identifier [cite: 385]
    val nama: String,         // Nama Lengkap [cite: 385]
    val prodi: String,        // Program Studi [cite: 385]
    val angkatan: Int,        // Tahun Angkatan [cite: 385]
    val status: String        // Tingkat Kaderisasi [cite: 385]
)

// UI State: Menangani status validasi form input [cite: 431]
data class UIStateKader(
    val detailKader: DetailKader = DetailKader(),
    val isEntryValid: Boolean = false
)

// DetailKader: Helper untuk menampung data sementara dari TextField [cite: 435]
@Serializable
data class DetailKader(
    val id_kader: Int = 0,
    val nim: String = "",
    val nama: String = "",
    val prodi: String = "",
    val angkatan: String = "", // Menggunakan String agar mudah di-input di UI
    val status: String = ""
)

// --- Fungsi Mapper (Konversi Data) ---

// Mengubah DetailKader (UI) menjadi DataKader (Database/API) [cite: 441]
fun DetailKader.toDataKader(): DataKader = DataKader(
    id_kader = id_kader,
    nim = nim,
    nama = nama,
    prodi = prodi,
    angkatan = angkatan.toIntOrNull() ?: 0,
    status = status
)

// Mengubah DataKader menjadi UIStateKader [cite: 447]
fun DataKader.toUIStateKader(isEntryValid: Boolean = false): UIStateKader = UIStateKader(
    detailKader = this.toDetailKader(),
    isEntryValid = isEntryValid
)

// Mengubah DataKader menjadi DetailKader untuk keperluan Edit Form [cite: 453]
fun DataKader.toDetailKader(): DetailKader = DetailKader(
    id_kader = id_kader,
    nim = nim,
    nama = nama,
    prodi = prodi,
    angkatan = angkatan.toString(),
    status = status
)