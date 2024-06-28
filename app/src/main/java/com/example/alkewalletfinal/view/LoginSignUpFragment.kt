package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentLoginSignUpBinding

/**
 * Fragmento que gestiona la pantalla de selección entre iniciar sesión y registrarse.
 * Permite a los usuarios navegar entre el fragmento de inicio de sesión y el fragmento de registro.
 */
class LoginSignUpFragment : Fragment() {

    //Instancia de binding
    private lateinit var binding: FragmentLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navegar hacia el fragmento de inicio de sesión.
        binding.text2p2.setOnClickListener{
            findNavController().navigate(R.id.action_loginSignUpFragment_to_loginFragment)
        }

        //Navegar hacia el fragmento de registro
        binding.btnP2.setOnClickListener{
            findNavController().navigate(R.id.action_loginSignUpFragment_to_signUpFragment)
        }
    }

}