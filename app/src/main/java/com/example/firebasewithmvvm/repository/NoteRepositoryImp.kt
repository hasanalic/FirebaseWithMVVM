package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.Note

class NoteRepositoryImp: NoteRepository {

    override fun getNotes(): List<Note> {
        // firebase'den veri çekilir ve return edilir.
        return arrayListOf()
    }
}