package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class NoteRepositoryImp(
    val database: FirebaseFirestore
): NoteRepository {

    override fun getNotes(): List<Note> {

        return arrayListOf(Note(
            "123456789",
            "textextextextextextext",
            Date()
        ))
    }
}