package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.DiaryViewModel
import com.example.myapplication.Screen

@Composable
fun RandomQuestionScreen(
    navController: NavHostController,
    viewModel: DiaryViewModel
) {
    val currentQuestion by remember { derivedStateOf { viewModel.currentQuestion.value } }
    val currentAnswer by remember { derivedStateOf { viewModel.currentAnswer.value } }
    var answerText by remember { mutableStateOf(currentAnswer) }

    // 當畫面載入時，如果沒有問題就生成一個
    LaunchedEffect(Unit) {
        if (currentQuestion.isEmpty()) {
            viewModel.generateRandomQuestion()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 標題
        Text(
            text = "寫下想法",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // 問題卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "今日問題：",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = currentQuestion,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 回答輸入框 - 簡化版本，不使用 keyboardOptions
        OutlinedTextField(
            value = answerText,
            onValueChange = {
                answerText = it
                viewModel.setAnswer(it)
            },
            label = { Text("請在這裡寫下您的回答...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            singleLine = false,
            maxLines = 10,
            placeholder = { Text("輸入您的想法...") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 按鈕行
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 跳過按鈕
            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.MainList.route)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("跳過")
            }

            // 保存按鈕
            Button(
                onClick = {
                    // 保存記錄到 ViewModel
                    viewModel.saveDiaryRecord()
                    // 導航回主畫面
                    navController.navigate(Screen.MainList.route)
                },
                modifier = Modifier.weight(1f),
                enabled = answerText.isNotEmpty()
            ) {
                Text("保存記錄", style = MaterialTheme.typography.titleMedium)
            }
        }

        // 提示文字
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "提示：系統會自動保存",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline
        )
    }
}