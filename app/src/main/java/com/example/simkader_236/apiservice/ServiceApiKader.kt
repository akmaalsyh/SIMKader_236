package com.example.simkader_236.apiservice

import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.modeldata.AuthDataKader // Menggunakan nama baru
import retrofit2.Response
import retrofit2.http.*

interface ServiceApiKader {

    @POST("register.php")
    suspend fun register(@Body user: Map<String, String>): Response<AuthDataKader>

    // Di dalam ServiceApiKader.kt
    @POST("login.php")
    suspend fun login(@Body kredensial: Map<String, String>): Response<AuthDataKader> // Pakai AuthDataKader

    // --- CRUD KADER ---
    @GET("bacaKader.php")
    suspend fun getKader(): List<DataKader>

    @GET("detailKader.php")
    suspend fun getSatuKader(@Query("id_kader") id: Int): DataKader

    @POST("insertKader.php")
    suspend fun postKader(@Body kader: DataKader): Response<Void>

    @PUT("editKader.php")
    suspend fun editSatuKader(@Query("id_kader") id: Int, @Body kader: DataKader): Response<Void>

    @DELETE("deleteKader.php")
    suspend fun hapusSatuKader(@Query("id_kader") id: Int): Response<Void>
}