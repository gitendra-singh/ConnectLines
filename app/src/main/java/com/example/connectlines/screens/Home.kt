package com.example.connectlines.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.connectlines.item_view.LineItem
import com.example.connectlines.utils.SharedPref
import com.example.connectlines.viewModel.HomeViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.time.Duration.Companion.seconds

@Composable
fun Home(navHostController: NavHostController) {

    val context = LocalContext.current

    val homeViewModel:HomeViewModel = viewModel()
    val linesAndUsers  by homeViewModel.linesAndUsers.observeAsState(null)

    LazyColumn {
        items(linesAndUsers?: emptyList()) {pairs ->
            LineItem(line = pairs.first, users = pairs.second, navHostController, FirebaseAuth.getInstance().currentUser!!.uid)
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ShowHome(){
//    Home()
}