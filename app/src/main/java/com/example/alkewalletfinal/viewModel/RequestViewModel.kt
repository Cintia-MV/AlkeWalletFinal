package com.example.alkewalletfinal.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


enum class ErroresIngresoMonto {
    MONTO_INVALIDO,
    NOTA_INVALIDA
}
class RequestViewModel : ViewModel(){
    // LiveData para observar el estado de los errores
    private val _ingresoMontoError = MutableLiveData<ErroresIngresoMonto?>()
    val ingresoMontoError: LiveData<ErroresIngresoMonto?> = _ingresoMontoError

    // LiveData para observar el éxito
    private val _ingresoMontoExitoso = MutableLiveData<Boolean>()
    val ingresoMontoExitoso: LiveData<Boolean> = _ingresoMontoExitoso

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


    private fun validarMontoIngreso(monto: String): Boolean{
        val montoIngrRegex = Regex("^[0-9]+(\\.[0-9]{1,2})?\$")
        return montoIngrRegex.matches(monto)
    }

    private fun validarNotaIngreso(nota: String):Boolean{
        return nota.isNotBlank()

    }
}