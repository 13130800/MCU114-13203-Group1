package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.Screen
import com.example.myapplication.UserManager

@Composable
fun LoginScreen(
    navController: NavHostController,
    userManager: UserManager
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 標題
        Text(
            text = "登入",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        // 錯誤訊息
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // 帳號輸入
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("帳號") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 密碼輸入
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密碼") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 登入按鈕
        Button(
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    // 模擬登入驗證（實際應該檢查資料庫）
                    if (username == "test" && password == "123") {
                        userManager.login(username)
                        navController.navigate(Screen.MainList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    } else {
                        // 簡單驗證：任何非空帳號密碼都可登入
                        userManager.login(username)
                        navController.navigate(Screen.MainList.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                } else {
                    errorMessage = "請輸入帳號和密碼"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("登入", style = MaterialTheme.typography.bodyLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 註冊連結
        TextButton(
            onClick = { navController.navigate(Screen.Register.route) }
        ) {
            Text("註冊新帳號")
        }
    }
}