package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import com.example.simkader_236.uicontroller.route.DestinasiDetail
import kotlinx.coroutines.launch

sealed interface DetailUiState {
    data class Success(val kader: DataKader) : DetailUiState
    object Error : DetailUiState
    object Loading : DetailUiState
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    private val kaderId: Int = checkNotNull(savedStateHandle[DestinasiDetail.kaderIdArg])

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    init {
        getKaderById()
    }

    // Fungsi ini akan dipanggil setiap kali halaman detail muncul (on-resume)
    fun getKaderById() {
        viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            try {
                val kader = repositoriDataKader.getSatuKader(kaderId)
                detailUiState = DetailUiState.Success(kader)
            } catch (e: Exception) {
                detailUiState = DetailUiState.Error
            }
        }
    }

    // Menambah onShowMessage agar konsisten dengan Entry & Edit
    fun deleteKader(onSuccess: () -> Unit, onShowMessage: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repositoriDataKader.hapusSatuKader(kaderId)
                if (response.isSuccessful) {
                    onShowMessage("Data kader berhasil dihapus")
                    onSuccess()
                } else {
                    onShowMessage("Gagal menghapus data dari server")
                }
            } catch (e: Exception) {
                onShowMessage("Kesalahan Jaringan: ${e.message}")
            }
        }
    }
}