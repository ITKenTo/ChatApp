package com.example.chat_app_k.Login_Register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private var image:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_register)
        binding= ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        binding.apply {
            btnRes.setOnClickListener {
                validate()

            }

            tvLogin.setOnClickListener {
               startActivity( Intent(this@RegisterActivity,LoginActivity::class.java))
            }
        }
    }

    private fun RegisterAccount(email:String, passwd:String){

        auth.createUserWithEmailAndPassword(email, passwd)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user:FirebaseUser? = auth.currentUser
                    val uID:String= user!!.uid
                    val account= AccountModel(uID,email,binding.edFullnameRes.text.toString(),passwd, image.toString(),1)
                    FirebaseDatabase.getInstance().getReference("Users").child(uID)
                        .setValue(account).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                finish()
                            }
                        }.addOnFailureListener { e ->
                            Toast.makeText(
                                this@RegisterActivity,
                                e.message.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
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
    fun isValidEmail(email: String): Boolean {
        val regex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return regex.matches(email)
    }
    private fun validate(){
        binding.apply {
            if (edFullnameRes.text.toString().isEmpty()){
                Toast.makeText(this@RegisterActivity,"Vui Lòng Nhập Tên",Toast.LENGTH_SHORT).show()
                return
            }

            if (edEmailRes.text.toString().isEmpty()){
                Toast.makeText(this@RegisterActivity,"Vui Lòng Nhập Email",Toast.LENGTH_SHORT).show()
                return
            }
            if (!isValidEmail(edEmailRes.text.toString())){
                Toast.makeText(this@RegisterActivity,"Định dạng email không hợp lệ",Toast.LENGTH_SHORT).show()
                return
            }
            if (edPasswdRes.text.toString().isEmpty()){
                Toast.makeText(this@RegisterActivity,"Vui Lòng Nhập Pass ",Toast.LENGTH_SHORT).show()
                return
            }
            if (edPasswdRes.text.toString().length<6){
                Toast.makeText(this@RegisterActivity,"Mật Khẩu Ít Nhất Phải Từ 6 Kí Tự Trở Lên ",Toast.LENGTH_SHORT).show()
                return
            }

            if (edPasswdResConfim.text.toString().isEmpty()){
                Toast.makeText(this@RegisterActivity,"Vui Lòng Nhập Lại Pass ",Toast.LENGTH_SHORT).show()
                return
            }

            if (edPasswdResConfim.text.toString()!=edPasswdRes.text.toString()) {
                Toast.makeText(this@RegisterActivity,"Mật khẩu không trùng khớp ",Toast.LENGTH_SHORT).show()
                return
            }


            RegisterAccount(edEmailRes.text.toString(),edPasswdRes.text.toString())
        }
    }
}