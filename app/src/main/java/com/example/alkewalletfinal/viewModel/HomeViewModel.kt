package com.example.alkewalletfinal.viewModel


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.AccountsResponse
import com.example.alkewalletfinal.model.response.Transactions
import com.example.alkewalletfinal.model.response.TransactionsResponse
import com.example.alkewalletfinal.model.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val authManager: AuthManager): ViewModel() {

    val userResponseLiveData: MutableLiveData<UserResponse> = MutableLiveData()
    val accountsResponse: MutableLiveData<List<AccountsResponse>> = MutableLiveData()
    val transactionsResponseLiveData: MutableLiveData<List<Transactions>> = MutableLiveData()
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

            RetrofitClient.retrofitInstance(authToken).infoAccounts("Bearer $authToken")
                .enqueue(object : Callback<List<AccountsResponse>>{
                    override fun onResponse(
                        call: Call<List<AccountsResponse>>,
                        response: Response<List<AccountsResponse>>
                    ) {
                        if (response.isSuccessful){
                            accountsResponse.postValue(response.body())
                            Log.d("HomeViewModel", "Account response: ${response.body()}")
                        } else {
                            errorLiveData.postValue("Error: ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<List<AccountsResponse>>, t: Throwable) {
                        errorLiveData.postValue("Error: ${t.message}")
                        Log.e("HomeViewModel", "Account Error: ${t.message}")
                    }
                })


            RetrofitClient.retrofitInstance(authToken).infoTransactions("Bearer $authToken")
                .enqueue(object : Callback<TransactionsResponse>{
                    override fun onResponse(
                        call: Call<TransactionsResponse>,
                        response: Response<TransactionsResponse>
                    ) {
                        if (response.isSuccessful){
                            transactionsResponseLiveData.postValue(response.body()?.data)
                            Log.d("HomeViewModel", "Transactions response: ${response.body()}")
                        } else {
                            errorLiveData.postValue("Error: ${response.code()} ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<TransactionsResponse>, t: Throwable) {
                        errorLiveData.postValue("Error: ${t.message}")
                        Log.e("HomeViewModel", "Transactions Error: ${t.message}")
                    }
                })
        }
    }
}