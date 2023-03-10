package com.example.firebasewithmvvm.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import com.example.firebasewithmvvm.R
import com.example.firebasewithmvvm.databinding.FragmentNoteListingBinding
import com.example.firebasewithmvvm.model.Note
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteListingFragment : Fragment() {

    private var _binding: FragmentNoteListingBinding? = null
    private val binding get() = _binding!!
    val viewModel: NoteViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteListingBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // önce viewmodel'daki "repo'dan verileri çeken fonksiyon" çalıştırılır.
        viewModel.getNotes()

        // viewmodel'daki getNotes() fonksiyonu, verileri yine viewmodel'daki _notes'a aktarır.
        // biz de "notes" liveData'sını observe ederek verinin durumunu takip ederiz.
        viewModel.notes.observe(viewLifecycleOwner){
            setViews(it)
        }
    }

    fun setViews(list: List<Note>) {
        binding.id.text = list[0].id
        binding.text.text = list[0].text
        binding.date.text = list[0].date.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}