package com.example.connectlines.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectlines.model.LineModel
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage
import java.util.UUID

class AddLineViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("lines")

    private val storageRef = Firebase.storage.reference
    private val imageRef = storageRef.child("lines/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted: LiveData<Boolean> = _isPosted

    fun saveImage(line: String, userId: String, imageUri: Uri) {

        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(line, userId, it.toString())
            }
        }
    }

    fun saveData(
        line: String,
        userId: String,
        image: String,
    ) {
        val lineData = LineModel(line, image, userId, System.currentTimeMillis().toString())

        userRef.child(userRef.push().key!!).setValue(lineData)
            .addOnSuccessListener {

                _isPosted.postValue(true)
            }.addOnFailureListener {
                _isPosted.postValue(false)
            }
    }
}