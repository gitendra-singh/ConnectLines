package com.example.connectlines.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.connectlines.screens.AddLines
import com.example.connectlines.screens.BottomNav
import com.example.connectlines.screens.Home
import com.example.connectlines.screens.Login
import com.example.connectlines.screens.Notifications
import com.example.connectlines.screens.OtherUsers
import com.example.connectlines.screens.Profile
import com.example.connectlines.screens.Register
import com.example.connectlines.screens.Search
import com.example.connectlines.screens.Splash

@Composable
fun NavGraph(navController: NavHostController){
    
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.routes
    ) {
        composable(Routes.Splash.routes) {
            Splash(navController)
        }

        composable(Routes.Home.routes) {
            Home(navController)
        }

        composable(Routes.Notifications.routes) {
            Notifications()
        }

        composable(Routes.Search.routes) {
            Search(navController)
        }

        composable(Routes.AddLines.routes) {
            AddLines(navController)
        }

        composable(Routes.Profile.routes) {
            Profile(navController)
        }

        composable(Routes.BottomNav.routes) {
            BottomNav(navController)
        }

        composable(Routes.Login.routes) {
            Login(navController)
        }

        composable(Routes.Register.routes) {
            Register(navController)
        }

        composable(Routes.OtherUsers.routes) {
            val data = it.arguments!!.getString("data")
            OtherUsers(navController, data!!)
        }

    }

}