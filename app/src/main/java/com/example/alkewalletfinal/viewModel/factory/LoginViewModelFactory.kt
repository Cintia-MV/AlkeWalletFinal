package com.example.alkewalletfinal.viewModel.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.viewModel.LoginViewModel

/**
 * Factory para crear instancias de [LoginViewModel].
 *
 * @property authManager El administrador de autenticación utilizado para gestionar el estado de autenticación y los tokens.
 * @property repository El repositorio que proporciona datos al ViewModel.
 */
class LoginViewModelFactory(private val context: Context, private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(repository,context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}