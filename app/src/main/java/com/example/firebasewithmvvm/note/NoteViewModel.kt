package com.example.firebasewithmvvm.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.firebasewithmvvm.model.Note
import com.example.firebasewithmvvm.repository.NoteRepository

class NoteViewModel(
    val repository: NoteRepository
): ViewModel() {

    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>>
        get() = _notes

    fun getNotes() {
        // repository'deki "getNotes" fonksiyonu bize List<Note> döndürür
        _notes.value = repository.getNotes()
    }
}