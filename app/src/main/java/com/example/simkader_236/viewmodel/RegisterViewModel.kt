package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch
import org.json.JSONObject

class RegisterViewModel(private val repositori: RepositoriDataKader) : ViewModel() {

    var namaLengkap by mutableStateOf("")
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("")

    var isLoading by mutableStateOf(false)
    var registerError by mutableStateOf<String?>(null)

    fun register(onSuccess: () -> Unit) {
        // 1. Validasi kecocokan password di sisi Client
        if (password != confirmPassword) {
            registerError = "Konfirmasi password tidak cocok!"
            return
        }

        // 2. Validasi kolom tidak boleh kosong
        if (namaLengkap.isBlank() || username.isBlank() || password.isBlank()) {
            registerError = "Semua kolom harus diisi!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            registerError = null
            try {
                val dataRegister = mapOf(
                    "nama" to namaLengkap.trim(),
                    "username" to username.trim(),
                    "password" to password,
                    "role" to "user"
                )

                val response = repositori.register(dataRegister)

                if (response.isSuccessful) {
                    // Berhasil mendaftar (Status 201)
                    onSuccess()
                } else {
                    // REVISI: Mengambil pesan error JSON dari PHP (Status 409 atau 400)
                    val errorJson = response.errorBody()?.string()
                    val message = try {
                        // Mencari teks di dalam JSON: {"message":"..."}
                        JSONObject(errorJson ?: "").getString("message")
                    } catch (e: Exception) {
                        "Pendaftaran gagal, silakan coba lagi."
                    }
                    registerError = message
                }
            } catch (e: Exception) {
                registerError = "Koneksi Gagal: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}