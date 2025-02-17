package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentProfileBinding

/**
 * Fragmento que muestra el perfil del usuario.
 * Permite al usuario navegar de regreso a la pantalla de inicio (HomePageFragment).
 */
class ProfileFragment : Fragment() {
    //Instancia de binding
    private lateinit var binding: FragmentProfileBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navegar de regreso a la pantalla de inicio (HomePageFragment).
        binding.volver.setOnClickListener{
            findNavController().navigate(R.id.action_profileFragment_to_homePageFragment)
        }
    }


}