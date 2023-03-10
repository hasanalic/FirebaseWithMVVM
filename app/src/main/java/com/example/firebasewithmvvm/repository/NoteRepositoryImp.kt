package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.Note
import com.example.firebasewithmvvm.util.FirestoreCollection
import com.example.firebasewithmvvm.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class NoteRepositoryImp(
    val database: FirebaseFirestore
): NoteRepository {

    override fun getNotes(result: (UiState<List<Note>>) -> Unit) {
        database.collection(FirestoreCollection.NOTE)
            .get()
            .addOnSuccessListener {
                val notes = arrayListOf<Note>()
                for (document in it) {
                    // "toObject" ile document -> model class'ımıza convert edilir.
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }
                result.invoke(
                    UiState.Success(notes)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun addNote(note: Note, result: (UiState<String>) -> Unit) {

        // "document()" Users'da yeni bir document oluşturur ve document'in referansını döndürür.
        val document = database.collection(FirestoreCollection.NOTE).document()
        // note objesinin id'sine yeni oluşturulan document'in id'si verilir.
        note.id = document.id

        // bu yeni oluşturulan document'a note objesi set edilir.
        document.set(note)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been created successfully!")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun updateNote(note: Note, result: (UiState<String>) -> Unit) {
        // update edeceğimiz document'in referansını aldık
        val document = database.collection(FirestoreCollection.NOTE).document(note.id)

        document.set(note)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been updated successfully!")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun deleteNote(note: Note, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreCollection.NOTE).document(note.id)
        document.delete()
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Note has been deleted successfully!")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

}