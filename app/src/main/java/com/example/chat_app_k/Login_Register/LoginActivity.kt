package com.example.chat_app_k.Login_Register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chat_app_k.MainActivity
import com.example.chat_app_k.R
import com.example.chat_app_k.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnLogin.setOnClickListener {
                startActivity( Intent(this@LoginActivity,MainActivity::class.java))
            }
            tvRegister.setOnClickListener {
                startActivity( Intent(this@LoginActivity,RegisterActivity::class.java))
            }
        }
    }
}