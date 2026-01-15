package com.example.simkader_236.viewmodel.provider

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simkader_236.repositori.AplikasiDataKader
import com.example.simkader_236.viewmodel.HomeViewModel
import com.example.simkader_236.viewmodel.EntryViewModel
import com.example.simkader_236.viewmodel.DetailViewModel
import com.example.simkader_236.viewmodel.EditViewModel
import com.example.simkader_236.viewmodel.GrafikKaderViewModel
import com.example.simkader_236.viewmodel.ListKaderViewModel
import com.example.simkader_236.viewmodel.LoginViewModel
import com.example.simkader_236.viewmodel.RegisterViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // Initializer untuk HomeViewModel
        initializer {
            HomeViewModel(
                aplikasiDataKader().container.repositoriDataKader
            )
        }

        // Initializer untuk EntryViewModel
        initializer {
            EntryViewModel(
                aplikasiDataKader().container.repositoriDataKader
            )
        }

        // Initializer untuk DetailViewModel (Membutuhkan SavedStateHandle untuk ID)
        initializer {
            DetailViewModel(
                this.createSavedStateHandle(),
                aplikasiDataKader().container.repositoriDataKader
            )
        }

        // Initializer untuk EditViewModel (Membutuhkan SavedStateHandle untuk ID)
        initializer {
            EditViewModel(
                this.createSavedStateHandle(),
                aplikasiDataKader().container.repositoriDataKader
            )
        }

        // Initializer untuk LoginViewModel
        initializer {
            LoginViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        // Initializer untuk RegisterViewModel
        initializer {
            RegisterViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

        // Tambahkan di dalam initializer PenyediaViewModel
        initializer {
            ListKaderViewModel(aplikasiDataKader().container.repositoriDataKader)
        }
        initializer {
            GrafikKaderViewModel(aplikasiDataKader().container.repositoriDataKader)
        }

    }
}

// Fungsi ekstensi untuk akses ke ContainerApp melalui kelas AplikasiDataKader [cite: 669-670]
fun CreationExtras.aplikasiDataKader(): AplikasiDataKader =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AplikasiDataKader)