package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch

class RegisterViewModel(private val repositori: RepositoriDataKader) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var confirmPassword by mutableStateOf("") // Ini harus ada!
    var isLoading by mutableStateOf(false)
    var registerError by mutableStateOf<String?>(null)

    // Di dalam RegisterViewModel.kt
    fun register(onSuccess: () -> Unit) {
        // Validasi internal sebelum tembak API
        if (password != confirmPassword) {
            registerError = "Password tidak cocok!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            registerError = null
            try {
                val response = repositori.register(
                    mapOf("username" to username, "password" to password, "role" to "user")
                )
                // Log untuk debug di Logcat
                println("Response Register: ${response.code()}")

                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    registerError = "Username sudah terdaftar!"
                }
            } catch (e: Exception) {
                registerError = "Koneksi Gagal: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}