package com.uts.remedy.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel(private val auth: FirebaseAuth) : ViewModel() {
    val userLiveData = MutableLiveData<FirebaseUser?>()

    fun login(email: String, password: String, function: (success: Boolean, message: String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { user ->
                userLiveData.postValue(user.user)
                function(true, null)
                Log.d("5556",email)
            }
            .addOnFailureListener { exception ->
                function(false, exception.message)
            }
    }

    fun signup(name: String, email: String, password: String, function: (success: Boolean, message: String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { user ->
                userLiveData.postValue(user.user)
                function(true, null)
            }
            .addOnFailureListener { exception ->
                function(false, exception.message)
            }
    }

    fun signOut() {
        auth.signOut()
    }
}

class AuthRepository(private val auth: FirebaseAuth) {
    fun login(email: String, password: String) = auth.signInWithEmailAndPassword(email, password)
    fun signup(email: String, password: String) = auth.createUserWithEmailAndPassword(email, password)
    fun signOut() = auth.signOut()
}
