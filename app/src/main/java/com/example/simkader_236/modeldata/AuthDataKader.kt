package com.example.simkader_236.modeldata

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataKader(
    val status: String,
    val message: String? = null,
    val user: DetailUserKader? = null
)

@Serializable
data class DetailUserKader(
    val id_user: String, // Sesuaikan dengan log: "1" adalah String
    val username: String,
    val role: String
)