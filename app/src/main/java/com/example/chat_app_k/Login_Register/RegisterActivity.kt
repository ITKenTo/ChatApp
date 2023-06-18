package com.example.chat_app_k.Login_Register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat_app_k.MainActivity
import com.example.chat_app_k.R
import com.example.chat_app_k.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnRes.setOnClickListener {
            }

            tvLogin.setOnClickListener {
                startActivity( Intent(this@RegisterActivity,LoginActivity::class.java))
            }
        }

    }
}