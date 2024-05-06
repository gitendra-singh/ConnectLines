package com.example.connectlines.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.connectlines.model.LineModel
import com.example.connectlines.model.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeViewModel: ViewModel() {

    private val db = FirebaseDatabase.getInstance()
    val line = db.getReference("lines")

    private var _linesAndUsers = MutableLiveData<List<Pair<LineModel, UserModel>>>()
    val linesAndUsers: LiveData<List<Pair<LineModel, UserModel>>> = _linesAndUsers

    init {
        fetchLinesAndUsers {
            _linesAndUsers.value = it
        }
    }

    private fun fetchLinesAndUsers(onResult: (List<Pair<LineModel, UserModel>>) -> Unit) {

        line.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val result = mutableListOf<Pair<LineModel, UserModel>>()
                for(lineSnapshot in snapshot.children) {

                    val line = lineSnapshot.getValue(LineModel::class.java)
                    line.let {
                        fetchUserFromLine(it!!){
                            user ->
                            result.add(0, it to user)

                            if (result.size == snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    fun fetchUserFromLine(line: LineModel, onResult: (UserModel) -> Unit) {
        db.getReference("users").child(line.userId)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val user = snapshot.getValue(UserModel::class.java)
                    user?.let(onResult)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }


}