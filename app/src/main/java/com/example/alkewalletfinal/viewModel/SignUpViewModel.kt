package com.example.alkewalletfinal.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.UserRequest
import com.example.alkewalletfinal.model.UserResponse

enum class ErroresSignUp {
    NOMBRE_INVALIDO,
    APELLIDO_INVALIDO,
    EMAIL_INVALIDO,
    CLAVE_INVALIDA
}
class SignUpViewModel: ViewModel() {
    // LiveData para observar el estado de los errores durante el registro.
    private val _signUpError = MutableLiveData<ErroresSignUp?>()
    val signUpError: LiveData<ErroresSignUp?> get() = _signUpError

    // LiveData para observar el éxito del registro.
    private val _signUpSuccess = MutableLiveData<Boolean>()
    val signUpSuccess: LiveData<Boolean> get() = _signUpSuccess

    // Método para agregar un nuevo usuario.
    fun agregarUsuario(nombre: String, apellido: String, email: String, clave: String, roledId: Int) {


        // Validar los campos de entrada
        if (!validarNombre(nombre)) {
            _signUpError.value = ErroresSignUp.NOMBRE_INVALIDO
            return
        }
        if (!validarApellido(apellido)) {
            _signUpError.value = ErroresSignUp.APELLIDO_INVALIDO
            return
        }
        if (!validarEmail(email)) {
            _signUpError.value = ErroresSignUp.EMAIL_INVALIDO
            return
        }
        if (!validarClave(clave)) {
            _signUpError.value = ErroresSignUp.CLAVE_INVALIDA
            return
        }

        // Si todos los campos son válidos, crear un nuevo usuario y agregarlo
        val nuevoUsuario = UserRequest(nombre, apellido, email, clave, roledId)


        // Retornar null si no hay errores
        _signUpError.value = null
        _signUpSuccess.value = true
    }

    //Metodo para validar nombre
    private fun validarNombre(nombre: String): Boolean {
        val regex = Regex("[a-zA-ZáéíóúÁÉÍÓÚñÑ]+")
        return nombre.isNotBlank() && nombre.matches(regex)
    }

    //Metodo para validar apellido
    private fun validarApellido(apellido: String): Boolean {
        val regex = Regex("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s'-]+")
        return apellido.isNotBlank() && apellido.matches(regex)
    }

    //Metodo para validar correo electrónico
    private fun validarEmail(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
        return emailRegex.matches(email)
    }

    //Metodo para validar contraseña
    private fun validarClave(clave: String): Boolean {
        return clave.length >= 6
    }
}