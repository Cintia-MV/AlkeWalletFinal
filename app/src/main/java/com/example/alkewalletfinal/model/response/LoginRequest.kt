package com.example.alkewalletfinal.model.response

/**
 * Clase que representa la solicitud de inicio de sesión enviada a la API.
 * @author Cintia Muñoz V.
 */
data class LoginRequest(val email:String, val password: String)