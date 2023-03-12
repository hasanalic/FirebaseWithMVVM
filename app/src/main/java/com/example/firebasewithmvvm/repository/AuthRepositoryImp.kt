package com.example.firebasewithmvvm.repository

import com.example.firebasewithmvvm.model.User
import com.example.firebasewithmvvm.util.FirestoreCollection
import com.example.firebasewithmvvm.util.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepositoryImp(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore
): AuthRepository {

    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    result.invoke(
                        UiState.Success("Login Successfully.")
                    )
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure("Authentication failed, check email and password.")
                )
            }
    }

    override fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    updateUserInfo(user) {state ->
                        when(state) {
                            is UiState.Success -> {
                                result.invoke(
                                    UiState.Success("User registered successfully")
                                )
                            }
                            is UiState.Failure -> {
                                result.invoke(
                                    UiState.Failure(state.error)
                                )
                            }
                            else -> {}
                        }
                    }
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Invalid authentication.")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Authentication failed, password should be at least 6 characters."))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Authentication failed, invalid email entered."))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Authentication failed, email already registered."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        val document = database.collection(FirestoreCollection.USER).document()
        user.id = document.id
        document
            .set(user)
            .addOnSuccessListener{
                result.invoke(
                    UiState.Success("User has been updated successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(it.localizedMessage)
                )
            }
    }

    override fun forgotPassword(user: User, result: (UiState<String>) -> Unit) {

    }
}