package com.example.firebasewithmvvm.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewithmvvm.R
import com.example.firebasewithmvvm.databinding.FragmentForgotPasswordBinding
import com.example.firebasewithmvvm.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment: Fragment() {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.forgotPassBtn.setOnClickListener {
            if(validation()) {
                viewModel.forgotPassword(binding.emailEt.text.toString())
            }
        }
    }

    fun observer() {
        viewModel.forgotPassword.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.forgotPassBtn.text = ""
                    binding.forgotPassProgress.show()
                }
                is UiState.Failure -> {
                    binding.forgotPassBtn.text = "Send"
                    binding.forgotPassProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.forgotPassBtn.text = "Send"
                    binding.forgotPassProgress.hide()
                    toast(state.data)
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if(binding.emailEt.text.isNullOrEmpty()) {
            isValid = false
            toast("enter email")
        } else {
            if(!binding.emailEt.text.toString().isValidEmail()) {
                isValid = false
                toast("invalid email")
            }
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}