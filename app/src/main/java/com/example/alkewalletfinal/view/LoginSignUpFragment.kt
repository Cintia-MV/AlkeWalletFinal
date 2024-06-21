package com.example.alkewalletfinal.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alkewalletfinal.R
import com.example.alkewalletfinal.databinding.FragmentLoginSignUpBinding


class LoginSignUpFragment : Fragment() {

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
        binding.text2p2.setOnClickListener{
            findNavController().navigate(R.id.action_loginSignUpFragment_to_loginFragment)
        }

        binding.btnP2.setOnClickListener{
            findNavController().navigate(R.id.action_loginSignUpFragment_to_signUpFragment)
        }
    }


}