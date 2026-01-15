package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

// Sealed interface untuk mendefinisikan status UI (Loading, Success, Error)
sealed interface HomeUiState {
    data class Success(val kader: List<DataKader>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(
    private val repositoriDataKader: RepositoriDataKader
) : ViewModel() {

    // Menyimpan status UI saat ini [cite: 537]
    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getKader() // Mengambil data saat ViewModel pertama kali dibuat [cite: 538-540]
    }

    // Fungsi untuk mengambil data dari repositori [cite: 541-542]
    fun getKader() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                // Jika sukses, status menjadi Success dengan data dari MySQL [cite: 545-546]
                HomeUiState.Success(repositoriDataKader.getDataKader())
            } catch (e: IOException) {
                // Error koneksi internet [cite: 547-548]
                HomeUiState.Error
            } catch (e: HttpException) {
                // Error dari sisi server API [cite: 549-550]
                HomeUiState.Error
            }
        }
    }
}