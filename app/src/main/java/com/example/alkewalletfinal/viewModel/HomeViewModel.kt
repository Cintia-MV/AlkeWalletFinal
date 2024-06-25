package com.example.alkewalletfinal.viewModel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val authManager: AuthManager): ViewModel() {

    val userResponseLiveData: MutableLiveData<UserResponse> = MutableLiveData()
    val errorLiveData: MutableLiveData<String> = MutableLiveData()

    fun getUserData() {
        val token = authManager.getToken()

        token?.let { authToken ->
            RetrofitClient.retrofitInstance(authToken).infoUser("Bearer $authToken")
                .enqueue(object : Callback<UserResponse> {
                    override fun onResponse(
                        call: Call<UserResponse>,
                        response: Response<UserResponse>
                    ) {
                        if (response.isSuccessful) {
                            userResponseLiveData.postValue(response.body())
                        } else {
                            errorLiveData.postValue("Error: ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                        errorLiveData.postValue("Error: ${t.message}")
                    }
                })
        }
    }
}