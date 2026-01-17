package com.example.simkader_236.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataKader(
    val status: String,
    val message: String? = null,
    // REVISI: Gunakan 'user' karena PHP mengirimkan {"user": {...}}
    val user: DetailUserKader? = null
)

@Serializable
data class DetailUserKader(
    val id_user: String,
    val nama: String? = "Admin", // Tambahkan null safety agar tidak crash
    val username: String,
    val role: String
)

@Serializable
data class ResponseKader(
    val status: String,
    val message: String
)