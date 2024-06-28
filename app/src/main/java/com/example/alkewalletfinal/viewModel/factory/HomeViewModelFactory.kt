package com.example.alkewalletfinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.viewModel.HomeViewModel

/**
 * Factory para crear instancias de [HomeViewModel].
 *
 * @property authManager El administrador de autenticación utilizado para gestionar el estado de autenticación y los tokens.
 * @property repository El repositorio que proporciona datos al ViewModel.
 */
class HomeViewModelFactory (
    private val authManager: AuthManager,
    private val repository: Repository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(authManager, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}