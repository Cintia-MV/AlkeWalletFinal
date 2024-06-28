package com.example.alkewalletfinal.model

import android.content.Context

class AuthManager (private  val context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String){
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String?{
        return prefs.getString("auth_token", null)
    }

    fun clearToken(){
        prefs.edit().remove("auth_token").apply()
    }

    fun clearUserLogged(){
        prefs.edit().remove("user_logged").apply()
    }

    fun saveUserId(userId: Long){
        prefs.edit().putLong("user_id", userId).apply()
    }

    fun getUserId(): Long{
        return prefs.getLong("user_id", -1)
    }

    fun clearUserId(){
        prefs.edit().remove("user_id").apply()
    }

    fun isLoggedIn(): Boolean {
        return getUserId() != -1L
    }

}