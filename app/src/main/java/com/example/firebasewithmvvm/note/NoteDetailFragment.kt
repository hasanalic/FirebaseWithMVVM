package com.example.firebasewithmvvm.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.firebasewithmvvm.R
import com.example.firebasewithmvvm.databinding.FragmentNoteDetailBinding
import com.example.firebasewithmvvm.model.Note
import com.example.firebasewithmvvm.util.UiState
import com.example.firebasewithmvvm.util.hide
import com.example.firebasewithmvvm.util.show
import com.example.firebasewithmvvm.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class NoteDetailFragment : Fragment() {

    private var _binding: FragmentNoteDetailBinding? = null
    private val binding get() = _binding!!
    val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNoteDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button2.setOnClickListener {
            if(validation()) {
                viewModel.addNote(Note(
                    id = "",
                    text = binding.noteMsg.text.toString(),
                    date = Date()
                ))
            }
        }

        viewModel.addNote.observe(viewLifecycleOwner) { state ->
            when(state) {
                is UiState.Loading -> {
                    binding.btnProgressBar.show()
                    binding.button2.text = ""
                }
                is UiState.Failure -> {
                    binding.btnProgressBar.hide()
                    binding.button2.text = "Create"
                    toast(state.error)
                }
                is UiState.Success -> {
                    binding.btnProgressBar.hide()
                    binding.button2.text = "Create"
                    toast(state.data)
                }
            }
        }
    }

    private fun validation(): Boolean{
        var isValid = true
        if(binding.noteMsg.text.toString().isNullOrEmpty()) {
            isValid = false
            toast("Enter message")
        }
        return isValid
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}