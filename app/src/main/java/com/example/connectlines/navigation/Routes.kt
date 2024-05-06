package com.example.connectlines.navigation

sealed class Routes(val routes: String) {

     object Home: Routes("home")
     object Notifications: Routes("notifications")
     object Profile: Routes("profile")
     object Search: Routes("search")
     object Splash: Routes("splash")
     object AddLines: Routes("add_lines")
     object BottomNav : Routes("bottom_nav")
     object Login : Routes("login")
     object Register : Routes("register")
     object OtherUsers : Routes("other_users/{data}")
}