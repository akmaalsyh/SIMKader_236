package com.example.simkader_236.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simkader_236.modeldata.DataKader
import com.example.simkader_236.repositori.RepositoriDataKader
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data class Success(val kader: List<DataKader>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel(private val repositoriDataKader: RepositoriDataKader) : ViewModel() {
    var homeUiState by mutableStateOf<HomeUiState>(HomeUiState.Loading)
        private set

    init {
        getStatistik() // Mengambil data saat aplikasi dibuka
    }

    fun getStatistik() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                // Mengambil data terbaru dari database MySQL
                HomeUiState.Success(repositoriDataKader.getDataKader())
            } catch (e: Exception) {
                HomeUiState.Error
            }
        }
    }
}