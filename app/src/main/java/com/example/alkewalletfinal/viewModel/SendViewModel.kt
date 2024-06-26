package com.example.alkewalletfinal.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alkewalletfinal.model.AuthManager
import com.example.alkewalletfinal.model.remote.RetrofitClient
import com.example.alkewalletfinal.model.response.TransactionsRequest
import com.example.alkewalletfinal.model.response.TransactionsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


enum class ErroresTransferencia {
    MONTO_INVALIDO,
    NOTA_INVALIDA
}
class SendViewModel(private val authManager: AuthManager) : ViewModel() {
    // LiveData para observar el estado de los errores
    private val _transferenciaError = MutableLiveData<ErroresTransferencia?>()
    val transferenciaError: LiveData<ErroresTransferencia?> = _transferenciaError

    // LiveData para observar el éxito
    private val _transferenciaExitosa = MutableLiveData<Boolean>()
    val transferenciaExitosa: LiveData<Boolean> = _transferenciaExitosa

    // LiveData para observar el resultado de la transacción
    private val _transactionResult = MutableLiveData<Boolean>()
    val transactionReult: LiveData<Boolean> = _transactionResult

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    // Método para validar la transferencia.
    fun validarTransferencia(monto:String, nota: String){
        if (!validarMonto(monto)){
            _transferenciaError.value = ErroresTransferencia.MONTO_INVALIDO
            return
        }

        if (!validarNota(nota)){
            _transferenciaError.value = ErroresTransferencia.NOTA_INVALIDA
            return
        }

        //Si todos los campos son válidos, realizar la transferencia cómo exitosa
        _transferenciaError.value = null
        _transferenciaExitosa.value = true
    }

    private fun validarMonto(monto: String): Boolean{
        val montoRegex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$")
        return montoRegex.matches(monto)
    }

    private fun validarNota(nota: String):Boolean{
        return nota.isNotBlank()
    }



    fun crearTransSend(transaccion: TransactionsRequest){
        val token = authManager.getToken()

        token?.let { authToken ->
            RetrofitClient.retrofitInstance(authToken).createTransaction("Bearer $authToken",transaccion)
                .enqueue(object : Callback<TransactionsResponse> {
                    override fun onResponse(
                        call: Call<TransactionsResponse>, response: Response<TransactionsResponse>
                    ) {
                       if (response.isSuccessful){
                           _transactionResult.postValue(true)
                       }else{
                           _transactionResult.postValue(false)
                           _errorLiveData.postValue("Error: ${response.code()} ${response.message()}")
                       }
                    }

                    override fun onFailure(call: Call<TransactionsResponse>, t: Throwable) {
                        _transactionResult.postValue(false)
                        _errorLiveData.postValue("Error: ${t.message}")
                    }
                })
        }
    }
}
