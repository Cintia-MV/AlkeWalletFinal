package com.example.alkewalletfinal.model

import android.content.Context

/**
 * Gestor para manejar el token de autenticación en las preferencias compartidas.
 *
 * @property context El contexto de la aplicación para acceder a las preferencias compartidas.
 * @author Cintia Muñoz V.
 */
class AuthManager (private  val context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    //Guarda el token de autenticación en las preferencias
    fun saveToken(token: String){
        prefs.edit().putString("auth_token", token).apply()
    }

    //Obtiene el token de autenticación guardado en las preferencias compartidas.
    fun getToken(): String?{
        return prefs.getString("auth_token", null)
    }

    //Borra el token de autenticación de las preferencias compartidas.
    fun clearToken(){
        prefs.edit().remove("auth_token").apply()
    }

}