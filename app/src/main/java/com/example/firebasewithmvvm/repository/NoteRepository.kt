package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.Note

interface NoteRepository {

    fun getNotes(): List<Note>
}