package com.example.alkewalletfinal.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.Repository
import com.example.alkewalletfinal.model.response.LoginRequest
import com.example.alkewalletfinal.model.response.LoginResponse
import kotlinx.coroutines.launch


/**
 * ViewModel para manejar la lógica de inicio de sesión.
 *
 * Este ViewModel gestiona la validación de credenciales de usuario, el inicio de sesión y el manejo de errores.
 *
 * @property repository Repositorio para acceder a los datos y realizar operaciones relacionadas con el inicio de sesión.
 * @property context Contexto de la aplicación para inicializar AuthManager.
 */
// Enum que representa los diferentes errores que pueden ocurrir durante el login.
enum class ErroresLogin{
    emailNoValido,
    claveNoValida,
    credencialesIncorrectas,
    errorDeRed
}
class LoginViewModel (private val repository: Repository, context: Context) : ViewModel(){

    // LiveData para observar el estado de la validación
    private val _loginError = MutableLiveData<ErroresLogin?>()
    val loginError: LiveData<ErroresLogin?> get() = _loginError

    //LiveData para observar el estado de la validación
    private val _loginResponse = MutableLiveData<LoginResponse?>()

    // LiveData para observar el éxito del login
    private val _loginSuccess = MutableLiveData<Boolean>()

    //Instancia de authManager
    private val authManager = AuthManager(context)

    // Método para validar las credenciales del usuario.
    fun validarUsuario(email: String, password: String) {
        // Validar email
        if (!validarEmail(email)) {
            _loginError.value = ErroresLogin.emailNoValido
            return
        }
        // Validar constraseña
        if (!validarClave(password)) {
            _loginError.value = ErroresLogin.claveNoValida
            return
        }

        iniciarSesion(email, password)
    }

    // Método privado para validar el formato del email.
    private fun validarEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
        return emailRegex.matches(email)
    }

    // Método privado para validar la longitud de la clave.
    private fun validarClave(clave: String): Boolean {
        return clave.length >= 6
    }

    //Función para el inicio de sesión
    private fun iniciarSesion(email: String, password: String) {
        val loginRequest = LoginRequest(email, password)

        viewModelScope.launch {
            try {
                val loginResponse = repository.login(loginRequest)

                // Procesar la respuesta del repositorio
                loginResponse?.let {
                    authManager.saveToken(it.accessToken)
                    _loginResponse.value = it
                    _loginError.value = null
                    _loginSuccess.value = true

                    Log.d("LoginViewModel", "Token recibido: ${it.accessToken}")

                    // Llamar a fetchAndSaveUser después del inicio de sesión exitoso
                    repository.fetchAndSaveUser(it.accessToken)
                    Log.d("LoginViewModel", "Información del usuario guardada")

                    Log.d("LoginViewModel", "Token recibido: ${it.accessToken}")
                }
            } catch (e: Exception) {
                _loginError.value = when (e.message) {
                    "Response is not successful" -> ErroresLogin.credencialesIncorrectas
                    else -> ErroresLogin.errorDeRed
                }
            }
        }
    }

}