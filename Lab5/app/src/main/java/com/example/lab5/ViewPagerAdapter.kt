package com.example.lab5

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 這個 Adapter 負責管理 ViewPager2 中的所有點餐頁面 (Fragment)。
 * 它定義了頁面的總數和每個位置對應的 Fragment。
 */
class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    // 總共有 4 個頁面：主頁面、主餐、副餐、飲料。
    override fun getItemCount(): Int = 4

    /**
     * 這個方法是 ViewPager2 的核心。
     * 我們根據 position (從 0 開始) 回傳對應的 Fragment 實例。
     */
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // 第 0 頁 (第一頁) -> 主頁面 / 確認訂單畫面
            0 -> ConfirmFragment()

            // 第 1 頁 (第二頁) -> 顯示選擇主餐的畫面
            1 -> MainMealFragment()

            // 第 2 頁 (第三頁) -> 顯示選擇副餐的畫面
            2 -> SideDishesFragment()

            // 第 3 頁 (第四頁) -> 顯示選擇飲料的畫面
            3 -> DrinkFragment()

            // 預設情況，以防萬一。
            else -> ConfirmFragment()
        }
    }
}

