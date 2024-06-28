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

/**
 * ViewModel para manejar la lógica relacionada con las solicitudes de transacción.
 *
 * Este ViewModel gestiona la validación de ingreso de monto y nota, así como la creación de transacciones a través de Retrofit.
 *
 * @property authManager Gestor de autenticación para obtener y manejar el token de autenticación.
 */
enum class ErroresIngresoMonto {
    MONTO_INVALIDO,
    NOTA_INVALIDA
}
class RequestViewModel(private val authManager: AuthManager) : ViewModel(){
    // LiveData para observar el estado de los errores
    private val _ingresoMontoError = MutableLiveData<ErroresIngresoMonto?>()
    val ingresoMontoError: LiveData<ErroresIngresoMonto?> = _ingresoMontoError

    // LiveData para observar el éxito
    private val _ingresoMontoExitoso = MutableLiveData<Boolean>()
    val ingresoMontoExitoso: LiveData<Boolean> = _ingresoMontoExitoso

    // LiveData para observar el resultado de la transacción
    private val _transactionResult = MutableLiveData<Boolean>()
    val transactionReult: LiveData<Boolean> = _transactionResult

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    // Método para validar el ingreso de monto y nota.
    fun validarIngresoMonto(monto: String, nota: String){
        if(!validarMontoIngreso(monto)){
            _ingresoMontoError.value = ErroresIngresoMonto.MONTO_INVALIDO
            return
        }

        if (!validarNotaIngreso(nota)){
            _ingresoMontoError.value = ErroresIngresoMonto.NOTA_INVALIDA
            return
        }

        _ingresoMontoError.value = null
        _ingresoMontoExitoso.value = true
    }

    //Método privado para validar el formato del monto ingresado.
    private fun validarMontoIngreso(monto: String): Boolean{
        val montoIngrRegex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$")
        return montoIngrRegex.matches(monto)
    }

    //Método privado para validar que la nota ingresada no esté en blanco.
    private fun validarNotaIngreso(nota: String):Boolean{
        return nota.isNotBlank()

    }

    //Método para crear una transacción utilizando Retrofit.
    fun createTransaction(transaccion: TransactionsRequest){
        val token = authManager.getToken()

        token?.let {authToken ->
            RetrofitClient.retrofitInstance(authToken).createTransaction("Bearer $authToken",transaccion)
                .enqueue(object : Callback<TransactionsResponse>{
                    override fun onResponse(
                        call: Call<TransactionsResponse>,
                        response: Response<TransactionsResponse>
                    ) {
                        if (response.isSuccessful){
                            _transactionResult.postValue(true)
                        } else {
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