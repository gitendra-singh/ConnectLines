package com.example.connectlines.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectlines.model.LineModel
import com.example.connectlines.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

class UserViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    private val lineRef = db.getReference("lines")
    private val userRef = db.getReference("users")

    private val _lines = MutableLiveData(listOf<LineModel>())
    val lines : LiveData<List<LineModel>> get() = _lines

    private val _followerList = MutableLiveData(listOf<String>())
    val followerList : LiveData<List<String>> get() = _followerList

    private val _followingList = MutableLiveData(listOf<String>())
    val followingList : LiveData<List<String>> get() = _followingList

    private val _users = MutableLiveData(UserModel())

    val users : MutableLiveData<UserModel> get() = _users

    fun fetchUser(uid : String) {
        userRef.child(uid).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _users.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    fun fetchLines(uid : String) {
    lineRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object: ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            val lineList = snapshot.children.mapNotNull {
                it.getValue(LineModel::class.java)
            }
            _lines.postValue(lineList)
        }

        override fun onCancelled(error: DatabaseError) {

        }

    })
}

    private val firestoreDb = Firebase.firestore
    fun followUsers(userId: String, currentUserId: String){

        val ref = firestoreDb.collection("following").document(userId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        ref.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
    }

    fun getFollowers(userId: String){

        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { value, error ->

                val followerIds = value?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)

            }

    }

    fun getFollowing(userId: String){

        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { value, error ->

                val followerIds = value?.get("followingIds") as? List<String> ?: listOf()
                _followingList.postValue(followerIds)

            }
    }


}