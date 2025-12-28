package com.example.myapplication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class UserManager : ViewModel() {
    // 用戶資訊
    var username = mutableStateOf("")
    var phone = mutableStateOf("")
    var gender = mutableStateOf("男")
    var email = mutableStateOf("")

    // 登入狀態
    var isLoggedIn = mutableStateOf(false)

    fun login(userAccount: String) {
        username.value = userAccount
        // 可以設置預設值
        if (phone.value.isEmpty()) {
            phone.value = "未設定"
        }
        if (email.value.isEmpty()) {
            email.value = "$userAccount@example.com"
        }
        isLoggedIn.value = true
    }

    fun logout() {
        isLoggedIn.value = false
    }

    fun updateProfile(newUsername: String, newPhone: String, newGender: String, newEmail: String) {
        username.value = newUsername
        phone.value = newPhone
        gender.value = newGender
        email.value = newEmail
    }
}