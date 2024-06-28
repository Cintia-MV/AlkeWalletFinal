package com.example.alkewalletfinal.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Fragmento utilizado para mostrar una pantalla de carga al iniciar la aplicación.
 * Realiza una navegación automática al fragmento de inicio de sesión o registro después de 3 segundos.
 */
class SplashScreenFragment : Fragment() {

    private lateinit var binding : FragmentSplashScreenBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Utilizar un CoroutineScope para iniciar una tarea con un retraso de 3000 milisegundos (3 segundos).
        CoroutineScope(Dispatchers.Main).launch {
            delay(3000)
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginSignUpFragment)
        }
    }

}