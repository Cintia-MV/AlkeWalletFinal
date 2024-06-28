package com.example.alkewalletfinal.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.viewModel.SendViewModel

/**
 * Factory para crear instancias de [SendViewModel].
 *
 * @property authManager El administrador de autenticación utilizado para gestionar el estado de autenticación y los tokens.
 */
class SendViewModelFactory(private val authManager: AuthManager): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SendViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SendViewModel(authManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}