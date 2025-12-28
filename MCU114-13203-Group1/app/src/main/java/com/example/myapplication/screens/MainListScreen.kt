package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.DiaryViewModel
import com.example.myapplication.Screen
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainListScreen(
    navController: NavHostController,
    viewModel: DiaryViewModel
) {
    // 觀察記錄列表的變化
    val diaryRecords by remember { derivedStateOf { viewModel.diaryRecords } }

    // 獲取當前時間
    val currentDate = remember {
        SimpleDateFormat("yyyy年MM月dd日", Locale.TAIWAN).format(Date())
    }
    val currentTime = remember {
        SimpleDateFormat("HH:mm", Locale.TAIWAN).format(Date())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // 時間顯示
                        Text(
                            text = "$currentTime $currentDate",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        // 標題
                        Text(
                            text = "我的心情日記",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                },
                actions = {
                    // 添加個人資訊按鈕
                    IconButton(onClick = { navController.navigate(Screen.Profile.route) }) {
                        Icon(Icons.Default.Person, contentDescription = "個人資訊")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screen.MoodSelection.route)
                },
                icon = { Icon(Icons.Default.Add, "新增") },
                text = { Text("新增紀錄") }
            )
        }
    ) { paddingValues ->
        if (diaryRecords.isEmpty()) {
            // 空狀態顯示
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.SentimentSatisfied,
                    contentDescription = "空狀態",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "還沒有任何紀錄",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Text(
                    text = "點擊右下角按鈕開始記錄",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(diaryRecords) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            // 時間和心情
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${record.date} ${record.time}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )

                                if (record.mood.isNotEmpty()) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // 根據心情文字決定表情符號
                                        val moodText = record.mood
                                        val moodEmoji = when {
                                            moodText.contains("開心") -> "😊"
                                            moodText.contains("關心") -> "🤔"
                                            moodText.contains("難過") -> "😔"
                                            moodText.contains("生氣") -> "😠"
                                            moodText.contains("平淡") -> "😐"
                                            moodText.contains("興奮") -> "🤩"
                                            else -> "😊"
                                        }

                                        Text(
                                            text = moodEmoji,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = record.mood,
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // 問題和回答
                            if (record.question.isNotEmpty()) {
                                Text(
                                    text = "Q: ${record.question}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            if (record.answer.isNotEmpty()) {
                                Text(
                                    text = record.answer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}