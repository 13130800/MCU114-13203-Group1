package com.example.myapplication.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.DiaryViewModel
import com.example.myapplication.Screen

// --- 數據類別優化 ---
data class MoodOption(
    val text: String,
    val emoji: String
)

data class MoodCategory(
    val category: String,
    val moods: List<MoodOption>
) {
    // 將分類圖標邏輯移至數據類，保持 UI 整潔
    val icon: String get() = when (category) {
        "學業" -> "📚"
        "感情" -> "❤️"
        "家庭" -> "🏠"
        "工作" -> "💼"
        "健康" -> "💪"
        else -> "📝"
    }
}

@Composable
fun MoodSelectionScreen(
    navController: NavHostController,
    viewModel: DiaryViewModel
) {
    // 定義統一的心情選項，避免重複代碼
    val defaultMoods = listOf(
        MoodOption("開心", "😊"),
        MoodOption("關心", "🤔"),
        MoodOption("難過", "😔"),
        MoodOption("生氣", "😠"),
        MoodOption("平淡", "😐"),
        MoodOption("興奮", "🤩")
    )

    val moodCategories = remember {
        listOf("學業", "感情", "家庭", "工作", "健康").map {
            MoodCategory(it, defaultMoods)
        }
    }

    var selectedMoods by remember { mutableStateOf<Map<String, MoodOption?>>(emptyMap()) }
    val showNextButton = selectedMoods.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            text = "選擇心情",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "選擇您今天在各方面的心情狀態",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(moodCategories) { category ->
                MoodCategorySection(
                    category = category,
                    selectedMood = selectedMoods[category.category],
                    onMoodSelected = { moodOption ->
                        selectedMoods = selectedMoods + (category.category to moodOption)
                        viewModel.setSelectedMood(category.category, "${moodOption.text}${moodOption.emoji}")
                    }
                )
            }
        }

        // 已選擇心情顯示區域
        if (selectedMoods.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("已選擇：", fontWeight = FontWeight.Bold)
                    selectedMoods.forEach { (cat, mood) ->
                        mood?.let { Text("• $cat: ${it.text}${it.emoji}") }
                    }
                }
            }
        }

        // 按鈕區域
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (showNextButton) {
                Button(
                    onClick = {
                        viewModel.generateRandomQuestion()
                        navController.navigate(Screen.RandomQuestion.route)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("下一步 ➡️")
                }
            }

            OutlinedButton(
                onClick = {
                    viewModel.generateRandomQuestion()
                    navController.navigate(Screen.RandomQuestion.route)
                },
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("跳過心情選擇 ⏭️")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class) // 必須添加此註解以使用 FlowRow
@Composable
fun MoodCategorySection(
    category: MoodCategory,
    selectedMood: MoodOption?,
    onMoodSelected: (MoodOption) -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Text(text = category.icon, style = MaterialTheme.typography.titleLarge)
                Text(text = category.category, fontWeight = FontWeight.Bold)
            }

            // 修正：導入 androidx.compose.foundation.layout.FlowRow
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                category.moods.forEach { moodOption ->
                    val isSelected = selectedMood?.text == moodOption.text

                    val chipColors = when (moodOption.text) {
                        "開心", "興奮" -> FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        "難過", "生氣" -> FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                        else -> FilterChipDefaults.filterChipColors()
                    }

                    FilterChip(
                        selected = isSelected,
                        onClick = { onMoodSelected(moodOption) },
                        label = { Text("${moodOption.text} ${moodOption.emoji}") },
                        colors = chipColors,
                        border = if (isSelected) {
                            FilterChipDefaults.filterChipBorder(
                                borderColor = MaterialTheme.colorScheme.primary,
                                borderWidth = 2.dp,
                                selected = true,
                                enabled = true
                            )
                        } else null
                    )
                }
            }
        }
    }
}
