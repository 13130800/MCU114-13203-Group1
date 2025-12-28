package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.Screen
import com.example.myapplication.UserManager

@Composable
fun ProfileScreen(
    navController: NavHostController,
    userManager: UserManager
) {
    // 從 UserManager 獲取用戶資料
    val currentUsername by remember { derivedStateOf { userManager.username.value } }
    val currentPhone by remember { derivedStateOf { userManager.phone.value } }
    val currentGender by remember { derivedStateOf { userManager.gender.value } }
    val currentEmail by remember { derivedStateOf { userManager.email.value } }

    // 本地狀態，用於編輯
    var username by remember { mutableStateOf(currentUsername) }
    var phone by remember { mutableStateOf(currentPhone) }
    var gender by remember { mutableStateOf(currentGender) }
    var email by remember { mutableStateOf(currentEmail) }
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 標題
        Text(
            text = "個人資訊",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 使用者名稱（不可更改的登入帳號）
        ProfileField(
            label = "登入帳號",
            value = currentUsername,
            isEditing = false,  // 登入帳號不可編輯
            onValueChange = { }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 姓名（可編輯）
        ProfileField(
            label = "顯示姓名",
            value = username,
            isEditing = isEditing,
            onValueChange = { username = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 電話
        ProfileField(
            label = "電話號碼",
            value = phone,
            isEditing = isEditing,
            onValueChange = { phone = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 性別
        if (isEditing) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "性別",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GenderOption(
                        text = "男",
                        isSelected = gender == "男",
                        onClick = { gender = "男" }
                    )
                    GenderOption(
                        text = "女",
                        isSelected = gender == "女",
                        onClick = { gender = "女" }
                    )
                    GenderOption(
                        text = "其他",
                        isSelected = gender != "男" && gender != "女",
                        onClick = { gender = "其他" }
                    )
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "性別",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = gender,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 電子郵件
        ProfileField(
            label = "電子郵件",
            value = email,
            isEditing = isEditing,
            onValueChange = { email = it }
        )

        Spacer(modifier = Modifier.height(48.dp))

        // 按鈕行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 編輯/完成按鈕
            Button(
                onClick = {
                    if (isEditing) {
                        // 保存更改
                        userManager.updateProfile(username, phone, gender, email)
                    }
                    isEditing = !isEditing
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isEditing) "完成" else "編輯")
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 登出按鈕
            OutlinedButton(
                onClick = {
                    userManager.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("登出")
            }
        }

        // 提示文字
        if (isEditing) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "提示：編輯後請點擊「完成」按鈕保存",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ProfileField(
    label: String,
    value: String,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary
        )

        if (isEditing) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun GenderOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(text) },
        colors = if (isSelected) {
            FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            FilterChipDefaults.filterChipColors()
        }
    )
}