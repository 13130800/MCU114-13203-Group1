package com.example.lab5

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.semantics.text
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.lab5.databinding.FragmentSideDishesBinding
import com.google.android.material.snackbar.Snackbar

class SideDishesFragment : Fragment() {

    private var _binding: FragmentSideDishesBinding? = null
    private val binding get() = _binding!!

    // 若要讓 activityViewModels() 正常運作，你必須在 build.gradle.kts 加入 'fragment-ktx' 函式庫
    private val viewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SideDishesFragment", "onCreateView: Fragment 視圖正在建立")
        _binding = FragmentSideDishesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("SideDishesFragment", "onViewCreated: Fragment 視圖已建立完成")

        setupDoneButton()
    }

    private fun setupDoneButton() {
        binding.btnDoneSideDishes.setOnClickListener {
            // [優化] 使用 functional programming 方式收集已選項目，更具擴展性
            val selectedDishes = listOf(binding.cbFries, binding.cbSalad, binding.cbCornCup)
                .filter { it.isChecked }
                .map { it.text.toString() }

            // 更新 ViewModel
            viewModel.setSideDishes(selectedDishes)

            // [優化] 簡化 if-else 邏輯，避免重複程式碼
            val message = if (selectedDishes.isNotEmpty()) {
                "Side dishes updated!"
            } else {
                "Side dish selection cleared."
            }
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

            // 無論選擇結果如何，最後都跳轉回主頁面
            (activity as? MainActivity)?.navigateToPage(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("SideDishesFragment", "onDestroyView: Fragment 視圖正在被銷毀")
        _binding = null
    }
}

