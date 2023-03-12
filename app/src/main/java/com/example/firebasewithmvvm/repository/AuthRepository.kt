package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.User
import com.example.firebasewithmvvm.util.UiState

interface AuthRepository {
    fun loginUser(user: User, result: (UiState<String>) -> Unit)
    fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: User, result: (UiState<String>) -> Unit)
    fun forgotPassword(user: User, result: (UiState<String>) -> Unit)
}