package com.example.simkader_236.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch

class LoginViewModel(private val repositori: RepositoriDataKader) : ViewModel() {
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var loginError by mutableStateOf<String?>(null)

    fun login(onSuccess: (String, String) -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            loginError = "Harap isi username dan password!"
            return
        }

        viewModelScope.launch {
            isLoading = true
            loginError = null

            try {
                val dataLogin = mapOf(
                    "username" to username.trim(),
                    "password" to password
                )

                val response = repositori.login(dataLogin)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.status == "success") {
                        // REVISI: Ambil role langsung dari objek 'user' sesuai log
                        val userRole = body.user?.role ?: "user"

                        // REVISI: Karena di JSON tidak ada field 'nama', gunakan 'username'
                        val namaUser = body.user?.username ?: username

                        // Log untuk memastikan di Logcat bahwa role yang didapat adalah 'user'
                        Log.d("LOGIN_DEBUG", "Login Berhasil! Username: $namaUser, Role: $userRole")

                        // Kirim data ke View (HalamanLogin)
                        onSuccess(userRole, namaUser)
                    } else {
                        loginError = body?.message ?: "Login Gagal"
                    }
                } else {
                    loginError = "Username atau Password salah!"
                }
            } catch (e: Exception) {
                Log.e("LOGIN_ERROR", "Detail: ${e.message}")
                loginError = "Terjadi kesalahan sistem saat membaca data."
            } finally {
                isLoading = false
            }
        }
    }
}