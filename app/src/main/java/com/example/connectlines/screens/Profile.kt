package com.example.connectlines.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.connectlines.item_view.LineItem
import com.example.connectlines.model.UserModel
import com.example.connectlines.navigation.Routes
import com.example.connectlines.utils.SharedPref
import com.example.connectlines.viewModel.AuthViewModel
import com.example.connectlines.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Profile(navHostController: NavHostController) {

    val authViewModel: AuthViewModel = viewModel()
    val firebaseUser by authViewModel.firebaseUser.observeAsState(null)

    val context = LocalContext.current

    val userViewModel: UserViewModel = viewModel()
    val lines by userViewModel.lines.observeAsState(null)

    val followerList by userViewModel.followerList.observeAsState(null)
    val followingList by userViewModel.followingList.observeAsState(null)

    var currentUserId = ""

    if(FirebaseAuth.getInstance().currentUser != null)
        currentUserId = FirebaseAuth.getInstance().currentUser!!.uid


    if (currentUserId != "") {
        userViewModel.getFollowers(currentUserId)
        userViewModel.getFollowing(currentUserId)
    }

    val user = SharedPref.getName(context)?.let {
        SharedPref.getImage(context)?.let { it1 ->
            SharedPref.getUserName(context)?.let { it2 ->
                UserModel(
                    name = it,
                    imageUrl = it1,
                    userName = it2
                )
            }
        }
    }

    if (firebaseUser != null) {
        userViewModel.fetchLines(firebaseUser!!.uid)
    }
    LaunchedEffect(key1 = firebaseUser) {
        if (firebaseUser == null) {
            navHostController.navigate(Routes.Login.routes) {
                popUpTo(navHostController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    }

    LazyColumn {
        item {
            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {

                val (text, logo, userName, bio, followers, following, button) = createRefs()


                SharedPref.getName(context)?.let {
                    Text(
                        text = it, style = TextStyle(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        ), modifier = Modifier.constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        })
                }

                Image(painter = rememberAsyncImagePainter(model = SharedPref.getImage(context)),
                    contentDescription = "logo",
                    modifier = Modifier
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
                        }
                        .size(120.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                SharedPref.getUserName(context)?.let {
                    Text(
                        it,
                        style = TextStyle(
                            fontSize = 20.sp
                        ), modifier = Modifier.constrainAs(userName) {
                            top.linkTo(text.bottom)
                            start.linkTo(parent.start)
                        })
                }

                SharedPref.getBio(context)?.let {
                    Text(
                        it,
                        style = TextStyle(
                            fontSize = 20.sp
                        ), modifier = Modifier.constrainAs(bio) {
                            top.linkTo(userName.bottom)
                            start.linkTo(parent.start)
                        })
                }

                    Text(
                        text = "${followerList!!.size} Followers",
                        style = TextStyle(
                            fontSize = 20.sp
                        ), modifier = Modifier.constrainAs(followers) {
                            top.linkTo(bio.bottom)
                            start.linkTo(parent.start)
                        })



                    Text(
                        text = "${followingList!!.size} Following",
                        style = TextStyle(
                            fontSize = 20.sp
                        ), modifier = Modifier.constrainAs(following) {
                            top.linkTo(followers.bottom)
                            start.linkTo(parent.start)
                        })

                ElevatedButton(onClick = {
                    authViewModel.logout()
                }, modifier = Modifier.constrainAs(button)
                {
                    top.linkTo(following.bottom)
                    start.linkTo(parent.start)
                }) {

                    Text(text = "Logout")
                }

            }

        }
        items(lines ?: emptyList() ){pair ->
            if (user != null) {
                SharedPref.getUserName(context)?.let { LineItem(line = pair, users = user, navHostController = navHostController, userId = it) }
            }
        }
    }

}