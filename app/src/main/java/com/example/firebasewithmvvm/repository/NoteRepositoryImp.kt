package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.Note
import com.google.firebase.firestore.FirebaseFirestore

class NoteRepositoryImp(
    val database: FirebaseFirestore
): NoteRepository {

    override fun getNotes(): List<Note> {
        // firebase'den veri çekilir ve return edilir.
        return arrayListOf()
    }
}