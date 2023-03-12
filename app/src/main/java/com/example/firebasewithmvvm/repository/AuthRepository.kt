package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.User
import com.example.firebasewithmvvm.util.UiState

interface AuthRepository {
    fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit)
    fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: User, result: (UiState<String>) -> Unit)
    fun forgotPassword(email: String, result: (UiState<String>) -> Unit)
}