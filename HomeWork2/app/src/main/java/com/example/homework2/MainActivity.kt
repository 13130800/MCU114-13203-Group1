package com.example.homework2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.net.Uri
import android.widget.TextView // 导入 TextView

class MainActivity : AppCompatActivity() {

    private lateinit var txtShow: TextView // 声明 TextView

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "拨打电话权限被拒绝", Toast.LENGTH_SHORT).show()
            }
        }


    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtShow = findViewById(R.id.txtShow) // 初始化 TextView

        // 初始化数字和小工具按钮
        setupNumberButtons()
        setupUtilButtons()
        setupCallButton()
    }

    @SuppressLint("SetTextI18n")
    private fun setupNumberButtons() {
        val numberButtonIds = listOf(
            R.id.btnZero, R.id.btnOne, R.id.btnTwo, R.id.btnThree, R.id.btnFour,
            R.id.btnFive, R.id.btnSix, R.id.btnSeven, R.id.btnEight, R.id.btnNine,
            R.id.btnStar
        )

        numberButtonIds.forEach { buttonId ->
            findViewById<Button>(buttonId).setOnClickListener {
                val button = it as Button
                val currentText = txtShow.text.toString()
                // 确保 "電話號碼：" 只在最开始，或者如果被清空了再添加
                if (currentText == "電話號碼：" || currentText.isEmpty()) {
                    txtShow.text = "電話號碼：${button.text}"
                } else {
                    txtShow.append(button.text)
                }
            }
        }
    }

    private fun setupUtilButtons() {
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            txtShow.text = "電話號碼：" // 重置文本
        }
    }

    private fun setupCallButton() {
        val callButton: Button = findViewById(R.id.callButton)
        // 更新呼叫按钮的文本可以动态进行，或者保持静态
        // callButton.text = "呼叫" // 或者你可以在这里动态设置

        callButton.setOnClickListener {
            if (!isTelephonyEnabled()) {
                Toast.makeText(this, "此设备无法拨打电话。", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val currentDisplayedText = txtShow.text.toString()
            val numberToDial = currentDisplayedText.removePrefix("電話號碼：").trim()

            if (numberToDial.isEmpty()) {
                Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    makePhoneCall(numberToDial)
                }
                shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE) -> {
                    Toast.makeText(this, "我们需要拨打电话权限才能继续。", Toast.LENGTH_LONG).show()
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
                }
            }
        }
    }


    @SuppressLint("UseKtx")
    private fun makePhoneCall(number: String? = null) { // 允许传入号码
        val numberToCall = number ?: txtShow.text.toString().removePrefix("電話號碼：").trim()

        if (numberToCall.isEmpty()) {
            // 如果在权限请求后，号码变为空（理论上不应该，但作为防御性编程）
            Toast.makeText(this, "电话号码为空", Toast.LENGTH_SHORT).show()
            return
        }

        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$numberToCall") // "tel:" 前缀很重要
        try {
            startActivity(callIntent)
        } catch (e: SecurityException) {
            Toast.makeText(this, "权限被拒绝或没有应用可以处理此操作。", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    private fun isTelephonyEnabled(): Boolean {
        val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        return telephonyManager.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }
}
