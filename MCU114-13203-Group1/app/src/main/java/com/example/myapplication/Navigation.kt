package com.example.myapplication

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object MainList : Screen("main_list")
    object RandomQuestion : Screen("random_question")
    object Profile : Screen("profile")
    object MoodSelection : Screen("mood_selection")
}