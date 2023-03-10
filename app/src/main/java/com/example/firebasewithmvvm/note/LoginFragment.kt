package com.example.firebasewithmvvm.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewithmvvm.R
import com.example.firebasewithmvvm.databinding.FragmentLoginBinding
import com.example.firebasewithmvvm.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.loginBtn.setOnClickListener {
            if(validation()) {
                viewModel.login(
                    binding.emailEt.text.toString(),
                    binding.passEt.text.toString()
                )
            }
        }

        binding.forgotPassLabel.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
        }
        binding.registerLabel.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    fun observer() {
        viewModel.login.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.loginBtn.text = ""
                    binding.loginProgress.show()
                }
                is UiState.Failure -> {
                    binding.loginBtn.text = "Login"
                    binding.loginProgress.hide()
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.loginBtn.text = "Login"
                    binding.loginProgress.hide()
                    toast(state.data)
                    findNavController().navigate(R.id.action_loginFragment_to_noteListingFragment)
                }
            }
        }
    }

    fun validation(): Boolean {
        var isValid = true

        if (binding.emailEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter email")
        }else{
            if (!binding.emailEt.text.toString().isValidEmail()){
                isValid = false
                toast("invalid email")
            }
        }
        if (binding.passEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter password")
        }else{
            if (binding.passEt.text.toString().length < 8){
                isValid = false
                toast("invalid password")
            }
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}