package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DetailKader
import com.example.simkader_236.modeldata.UIStateKader
import com.example.simkader_236.modeldata.toDataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json // Tambahkan ini untuk membaca pesan error

class EntryViewModel(
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    var uiStateKader by mutableStateOf(UIStateKader())
        private set

    fun updateUiState(detailKader: DetailKader) {
        uiStateKader = UIStateKader(
            detailKader = detailKader,
            isEntryValid = validasiInput(detailKader)
        )
    }

    private fun validasiInput(uiState: DetailKader = uiStateKader.detailKader): Boolean {
        return with(uiState) {
            nama.isNotBlank() && nim.isNotBlank() && prodi.isNotBlank() &&
                    angkatan.isNotBlank() && status.isNotBlank()
        }
    }

    // REVISI: Tambahkan parameter onShowMessage untuk menampilkan Pop-up
    fun simpanKader(onSuccess: () -> Unit, onShowMessage: (String) -> Unit) {
        if (validasiInput()) {
            viewModelScope.launch {
                try {
                    val response = repositoriDataKader.postDataKader(
                        uiStateKader.detailKader.toDataKader()
                    )

                    if (response.isSuccessful) {
                        // Jika status 201 (Berhasil)
                        onShowMessage("Data kader berhasil ditambahkan")
                        onSuccess()
                    } else {
                        // Menangkap pesan duplikasi dari PHP (misal: "NIM sudah terdaftar")
                        val errorJson = response.errorBody()?.string()
                        val message = try {
                            // Mencari kata setelah "message":" di dalam JSON error
                            errorJson?.split("\"message\":\"")?.get(1)?.split("\"")?.get(0)
                                ?: "Gagal: Terjadi duplikasi data"
                        } catch (e: Exception) {
                            "NIM atau Nama sudah digunakan"
                        }
                        onShowMessage(message)
                    }
                } catch (e: Exception) {
                    onShowMessage("Kesalahan: ${e.message}")
                }
            }
        }
    }
}