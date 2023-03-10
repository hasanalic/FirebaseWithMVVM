package com.example.firebasewithmvvm.di

import com.example.firebasewithmvvm.repository.NoteRepository
import com.example.firebasewithmvvm.repository.NoteRepositoryImp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideNoteRepository(database: FirebaseFirestore): NoteRepository {
        // "database: FirebaseFirestore" FirebaseModule'de zaten oluşturuldu
        return NoteRepositoryImp(database)
    }
}