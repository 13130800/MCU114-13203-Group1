package com.example.myapplication

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class DiaryViewModel : ViewModel() {
    // 保存所有日記記錄
    val diaryRecords = mutableStateListOf<DiaryRecord>()

    // 當前選擇的心情（在不同畫面間共享）
    var selectedMoods = mutableMapOf<String, String>()
        private set

    // 當前隨機問題
    var currentQuestion = mutableStateOf("")
        private set

    // 當前回答
    var currentAnswer = mutableStateOf("")
        private set

    // 生成隨機問題
    private val questions = listOf(
        "今天最想感謝的一件事是什麼？",
        "今天讓您感到最開心的是什麼？",
        "今天遇到了什麼挑戰？",
        "今天學到了什麼新東西？",
        "今天最想對自己說的一句話是什麼？",
        "今天有什麼事情讓您感到困擾？",
        "今天有什麼小確幸嗎？",
        "明天您最期待的是什麼？",
        "今天您對自己有什麼新認識？",
        "今天有什麼想要記錄下來的想法？"
    )

    fun setSelectedMood(category: String, mood: String) {
        selectedMoods[category] = mood
    }

    fun generateRandomQuestion() {
        currentQuestion.value = questions.random()
        currentAnswer.value = "" // 清空之前的回答
    }

    fun saveDiaryRecord() {
        if (selectedMoods.isNotEmpty() && currentQuestion.value.isNotEmpty() && currentAnswer.value.isNotEmpty()) {
            val dateFormat = SimpleDateFormat("yyyy年MM月dd日", Locale.TAIWAN)
            val timeFormat = SimpleDateFormat("HH:mm", Locale.TAIWAN)
            val now = Date()

            // 將所有心情合併成一個字串
            val moodText = selectedMoods.entries.joinToString("、") { "${it.key}: ${it.value}" }

            val newRecord = DiaryRecord(
                id = diaryRecords.size + 1,
                date = dateFormat.format(now),
                time = timeFormat.format(now),
                mood = moodText,
                category = selectedMoods.keys.joinToString("、"),
                question = currentQuestion.value,
                answer = currentAnswer.value
            )

            diaryRecords.add(0, newRecord) // 添加到最前面

            // 重置數據
            selectedMoods.clear()
            currentQuestion.value = ""
            currentAnswer.value = ""
        }
    }

    fun setAnswer(answer: String) {
        currentAnswer.value = answer
    }
}

data class DiaryRecord(
    val id: Int,
    val date: String,
    val time: String,
    val mood: String = "",
    val category: String = "",
    val question: String = "",
    val answer: String = ""
)