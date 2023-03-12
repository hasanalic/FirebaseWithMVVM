package com.example.firebasewithmvvm.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.firebasewithmvvm.R
import com.example.firebasewithmvvm.databinding.FragmentRegisterBinding
import com.example.firebasewithmvvm.model.User
import com.example.firebasewithmvvm.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment: Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding
        get() = _binding!!

    val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()

        binding.registerBtn.setOnClickListener {
            if(validation()) {
                viewModel.register(
                    email = binding.emailEt.text.toString(),
                    password = binding.passEt.text.toString(),
                    user = getUserObj()
                )
            }
        }

    }

    fun observer() {
        viewModel.register.observe(viewLifecycleOwner) {state ->
            when(state) {
                is UiState.Loading -> {
                    binding.registerProgress.show()
                    binding.registerBtn.text = ""
                }
                is UiState.Failure -> {
                    binding.registerProgress.hide()
                    binding.registerBtn.text = "Register"
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.registerProgress.hide()
                    binding.registerBtn.text = "Register"
                    toast(state.data)
                    findNavController().navigate(R.id.action_registerFragment_to_noteListingFragment)
                }
            }
        }
    }

    fun getUserObj(): User {
        return User(
            id = "",
            first_name = binding.firstNameEt.text.toString(),
            last_name = binding.lastNameEt.text.toString(),
            job_title = binding.jobTitleEt.text.toString(),
            email = binding.emailEt.text.toString()
        )
    }

    fun validation(): Boolean {
        var isValid = true

        if(binding.firstNameEt.text.isNullOrEmpty()) {
            isValid = false
            toast(getString(R.string.enter_first_name)) // getString() ile string.xml içindeki veriler çekilebilir.
        }
        if (binding.lastNameEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter last name")
        }

        if (binding.jobTitleEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter job title")
        }

        if (binding.emailEt.text.isNullOrEmpty()){
            isValid = false
            toast("enter email")
        } else{
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
                toast("invalid pass")
            }
        }
        return isValid
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}