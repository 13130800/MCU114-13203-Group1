package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.Screen
import com.example.myapplication.UserManager

@Composable
fun RegisterScreen(
    navController: NavHostController,
    userManager: UserManager
) {
    var username by remember { mutableStateOf("") }
    var account by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
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
            text = "註冊",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 錯誤訊息
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // 使用者名稱
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("使用者名稱") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 帳號
        OutlinedTextField(
            value = account,
            onValueChange = { account = it },
            label = { Text("帳號") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 密碼
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("密碼") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 確認密碼
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("確認密碼") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 註冊按鈕
        Button(
            onClick = {
                if (username.isEmpty() || account.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    errorMessage = "請填寫所有欄位"
                } else if (password != confirmPassword) {
                    errorMessage = "密碼不一致"
                } else {
                    // 註冊成功，自動登入並跳轉到主畫面
                    userManager.login(account)
                    navController.navigate(Screen.MainList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "註冊",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 返回登入按鈕
        TextButton(
            onClick = { navController.popBackStack() }
        ) {
            Text("已有帳號？返回登入")
        }
    }
}