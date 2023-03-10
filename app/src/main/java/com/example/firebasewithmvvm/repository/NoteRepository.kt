package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.Note
import com.example.firebasewithmvvm.util.UiState

interface NoteRepository {

    fun getNotes(result: (UiState<List<Note>>) -> Unit)

    fun addNote(note: Note, result: (UiState<String>) -> Unit)
}