package com.example.chat_app_k.Login_Register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chat_app_k.MainActivity
import com.example.chat_app_k.R
import com.example.chat_app_k.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding:ActivityLoginBinding
    private lateinit var mAth:FirebaseAuth

    override fun onStart() {
        super.onStart()
        val currentUser = mAth.currentUser
        if (currentUser != null) {
            val intent= Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAth=Firebase.auth
        binding.apply {
            btnLogin.setOnClickListener {
                validate()
            }
            tvRegister.setOnClickListener {
                startActivity( Intent(this@LoginActivity,RegisterActivity::class.java))
            }
        }
    }
    private fun LoginAccount(email:String, passwd:String){
        mAth.signInWithEmailAndPassword(email, passwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = mAth.currentUser
                    val intent= Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun validate(){
            binding.apply {
                if (edUsername.text.toString().isEmpty()){
                    Toast.makeText(this@LoginActivity,"Vui Lòng Nhập Email",Toast.LENGTH_SHORT).show()
                    return
                }

                if (edPasswd.text.toString().isEmpty()){
                    Toast.makeText(this@LoginActivity,"Vui Lòng Nhập Password",Toast.LENGTH_SHORT).show()
                    return
                }
                LoginAccount(edUsername.text.toString(),edPasswd.text.toString())
            }
        }
}