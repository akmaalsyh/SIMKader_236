package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DetailKader
import com.example.simkader_236.modeldata.UIStateKader
import com.example.simkader_236.modeldata.toDataKader
import com.example.simkader_236.modeldata.toUIStateKader
import com.example.simkader_236.repositori.RepositoriDataKader
import com.example.simkader_236.uicontroller.route.DestinasiEdit
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    private val kaderId: Int = checkNotNull(savedStateHandle[DestinasiEdit.kaderIdArg])

    var kaderUiState by mutableStateOf(UIStateKader())
        private set

    init {
        viewModelScope.launch {
            try {
                val kader = repositoriDataKader.getSatuKader(kaderId)
                kaderUiState = kader.toUIStateKader(isEntryValid = true)
            } catch (e: Exception) {
                println("Error Ambil Data: ${e.message}")
            }
        }
    }

    fun updateUiState(detailKader: DetailKader) {
        kaderUiState = UIStateKader(
            detailKader = detailKader,
            isEntryValid = validasiInput(detailKader)
        )
    }

    private fun validasiInput(uiState: DetailKader = kaderUiState.detailKader): Boolean {
        return with(uiState) {
            nama.isNotBlank() && nim.isNotBlank() && prodi.isNotBlank() &&
                    angkatan.isNotBlank() && status.isNotBlank()
        }
    }

    /**
     * REVISI: Menambahkan parameter onShowMessage agar sinkron dengan HalamanEdit.kt
     * Parameter ini bertugas mengirimkan pesan (Berhasil/Gagal) ke Pop-up
     */
    fun updateKader(onSuccess: () -> Unit, onShowMessage: (String) -> Unit) {
        if (validasiInput()) {
            viewModelScope.launch {
                try {
                    val response = repositoriDataKader.editSatuKader(
                        kaderId,
                        kaderUiState.detailKader.toDataKader()
                    )

                    if (response.isSuccessful) {
                        // Jika PHP mengirim status 200/201
                        onShowMessage("Data kader berhasil diperbarui")
                        onSuccess()
                    } else {
                        // Menangkap pesan error dari PHP (misal: NIM sudah ada)
                        val errorJson = response.errorBody()?.string()
                        val message = try {
                            // Mencoba mengambil pesan teks di antara tanda kutip "message":"..."
                            errorJson?.split("\"message\":\"")?.get(1)?.split("\"")?.get(0)
                                ?: "Gagal memperbarui data"
                        } catch (e: Exception) {
                            "NIM atau Nama sudah digunakan kader lain"
                        }
                        onShowMessage(message)
                    }
                } catch (e: Exception) {
                    onShowMessage("Terjadi kesalahan jaringan: ${e.message}")
                }
            }
        }
    }
}