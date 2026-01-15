package com.example.simkader_236.repositori

import com.example.simkader_236.apiservice.ServiceApiKader
import com.example.simkader_236.modeldata.AuthDataKader
import com.example.simkader_236.modeldata.DataKader
import retrofit2.Response

// Interface tunggal yang sekarang mencakup manajemen data Kader dan Autentikasi
interface RepositoriDataKader {
    // Manajemen Data Kader (CRUD)
    suspend fun getDataKader(): List<DataKader>
    suspend fun postDataKader(dataKader: DataKader): Response<Void>
    suspend fun getSatuKader(id: Int): DataKader
    suspend fun editSatuKader(id: Int, dataKader: DataKader): Response<Void>
    suspend fun hapusSatuKader(id: Int): Response<Void>

    // Manajemen Autentikasi (Login & Register)
    suspend fun register(user: Map<String, String>): Response<AuthDataKader>
    suspend fun login(kredensial: Map<String, String>): Response<AuthDataKader>
}

// Implementasi Jaringan
class JaringanRepositoriDataKader(
    private val serviceApiKader: ServiceApiKader
) : RepositoriDataKader {

    // Implementasi CRUD
    override suspend fun getDataKader(): List<DataKader> =
        serviceApiKader.getKader()

    override suspend fun postDataKader(dataKader: DataKader): Response<Void> =
        serviceApiKader.postKader(dataKader)

    override suspend fun getSatuKader(id: Int): DataKader =
        serviceApiKader.getSatuKader(id)

    override suspend fun editSatuKader(id: Int, dataKader: DataKader): Response<Void> =
        serviceApiKader.editSatuKader(id, dataKader)

    override suspend fun hapusSatuKader(id: Int): Response<Void> =
        serviceApiKader.hapusSatuKader(id)

    // Implementasi Autentikasi
    override suspend fun register(user: Map<String, String>): Response<AuthDataKader> {
        return serviceApiKader.register(user)
    }

    // Di dalam JaringanRepositoriDataKader.kt
    override suspend fun login(kredensial: Map<String, String>): Response<AuthDataKader> {
        return serviceApiKader.login(kredensial)
    }
}