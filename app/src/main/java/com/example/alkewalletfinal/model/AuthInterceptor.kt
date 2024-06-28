package com.example.alkewalletfinal.model

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor para agregar un token de autenticación a las solicitudes HTTP.
 *
 * @property token El token de autenticación que se agregará a las cabeceras de las solicitudes.
 * @author Cintia Muñoz V.
 */
class AuthInterceptor (private val token: String): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }

}