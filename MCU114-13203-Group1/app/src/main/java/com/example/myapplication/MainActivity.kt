package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.screens.*
import com.example.myapplication.ui.theme.MoodDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoodDiaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MoodDiaryApp()
                }
            }
        }
    }
}

@Composable
fun MoodDiaryApp() {
    val navController = rememberNavController()
    val diaryViewModel: DiaryViewModel = viewModel()
    val userManager: UserManager = viewModel()

    // 根據登入狀態決定起始頁面
    val startDestination = if (userManager.isLoggedIn.value) {
        Screen.MainList.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController, userManager)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController, userManager)
        }
        composable(Screen.MainList.route) {
            if (userManager.isLoggedIn.value) {
                MainListScreen(navController, diaryViewModel)
            } else {
                // 如果未登入，導向登入頁面
                LoginScreen(navController, userManager)
            }
        }
        composable(Screen.RandomQuestion.route) {
            if (userManager.isLoggedIn.value) {
                RandomQuestionScreen(navController, diaryViewModel)
            } else {
                LoginScreen(navController, userManager)
            }
        }
        composable(Screen.Profile.route) {
            if (userManager.isLoggedIn.value) {
                ProfileScreen(navController, userManager)
            } else {
                LoginScreen(navController, userManager)
            }
        }
        composable(Screen.MoodSelection.route) {
            if (userManager.isLoggedIn.value) {
                MoodSelectionScreen(navController, diaryViewModel)
            } else {
                LoginScreen(navController, userManager)
            }
        }
    }
}