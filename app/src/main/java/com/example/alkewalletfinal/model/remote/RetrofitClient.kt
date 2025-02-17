package com.example.alkewalletfinal.model.remote

import android.util.Log
import com.example.alkewalletfinal.model.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cliente Retrofit para configurar y obtener instancias de la API de la wallet.
 * @author Cintia Muñoz V.
 */
class RetrofitClient {

    companion object {
        private const val BASE_URL = "http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstalk.com/"
        private var retrofit: Retrofit? = null

        //Configura y devuelve una instancia de Retrofit para la API de la wallet
        fun getApi(token: String? = null): Retrofit {
            val clientBuilder = OkHttpClient.Builder()
            if (token != null) {
                clientBuilder.addInterceptor(AuthInterceptor(token))
                Log.i("USUARIO", "Interceptor con token añadido: $token")
            }
            val client = clientBuilder.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        //Instancia de la interfaz de la API de la wallet.
        fun retrofitInstance(token: String? = null): Api {
            if (retrofit == null) {
                retrofit = getApi(token)
            }
            return retrofit!!.create(Api::class.java)
        }
    }
}