package com.example.lab5

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class MainActivity : AppCompatActivity() {

    // 將 viewPager2 宣告為成員變數，以便在其他方法中存取
    private lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d("MainActivity", "onCreate") // (建議) 將 Log.e 改為 Log.d，因為這不是一個錯誤訊息

        // 取得 ViewPager2 元件
        viewPager2 = findViewById(R.id.viewPager2)

        // 建立 ViewPagerAdapter 並設定給 ViewPager2
        // FragmentStateAdapter 的建構子需要一個 FragmentActivity，直接傳入 'this' 即可。
        val adapter = ViewPagerAdapter(this)
        viewPager2.adapter = adapter

        // 預先載入鄰近的頁面，1 表示左右各預載一個頁面
        viewPager2.offscreenPageLimit = 2 // (建議) 設為2可以讓體驗更流暢

        // 預設情況下禁用滑動，使用者只能透過按鈕跳轉，確保流程正確
        viewPager2.isUserInputEnabled = false
    }

    /**
     * 提供一個公開的方法，讓 Fragment 可以呼叫來切換 ViewPager2 的頁面。
     * @param pageIndex 要跳轉到的頁面索引 (從 0 開始)。
     */
    fun navigateToPage(pageIndex: Int) {
        // 使用 setCurrentItem 方法來切換頁面，true 表示有平滑滾動動畫
        viewPager2.setCurrentItem(pageIndex, true)
    }


    // --- 以下為生命週期的 Log (用於除錯，保持不變) ---

    override fun onRestart() {
        super.onRestart()
        Log.d("MainActivity", "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("MainActivity","onStop")
    }



    override fun onDestroy() {
        super.onDestroy()
        Log.d("MainActivity","onDestroy")
    }
}
