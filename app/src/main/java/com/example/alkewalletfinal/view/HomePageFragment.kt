package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentHomePageBinding


class HomePageFragment : Fragment() {
    private lateinit var binding: FragmentHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Clic en la imagen para navegar hacia la configuración del perfil
       /* binding.imgArnold.setOnClickListener{
            findNavController().navigate(R.id.action_homePageFragment2_to_profileFragment)
        }

        //Clic en botón enviar dinero para navegar hacia la transacción
        hPBinding.btnEnviarDinP5.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePageFragment2_to_sendFragment)
        }

        hPBinding.btnIngresarDinP5.setOnClickListener {
            view.findNavController().navigate(R.id.action_homePageFragment2_to_requestFragment)
        }*/
    }


}
