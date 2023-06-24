package com.example.chat_app_k

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.databinding.ActivityChangePassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChangePassActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChangePassBinding
    private lateinit var databaseReference: DatabaseReference
    var passwd:String?=""
    private lateinit var mAth:FirebaseAuth
    private lateinit var accountcrr:FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_change_pass)
        binding=ActivityChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent= getIntent()
        passwd= intent.getStringExtra("Passwd")

        mAth= FirebaseAuth.getInstance()
        Log.d("Pass", passwd!!)

        binding.apply {
            btnUpPass.setOnClickListener {
                validate()
               // changePass()
            }
            back.setOnClickListener {
                onBackPressed()
            }
        }


    }

    private fun validate(){
        binding.apply {
            if (edPasswdOld.text.toString().isEmpty()){
                errorChange.setText("Nhập mật khẩu cũ")
                errorChange.visibility=View.VISIBLE
                return
            }
            if (edPasswdNew.text.toString().isEmpty()){
                errorChange.setText("Nhập mật khẩu mới")
                errorChange.visibility=View.VISIBLE
                return
            }

            if (edPasswdConfim.text.toString().isEmpty()){
                errorChange.setText("Nhập lại mật khẩu mới")
                errorChange.visibility=View.VISIBLE
                return
            }
            if (!edPasswdConfim.text.toString().equals(edPasswdNew.text.toString())){
                errorChange.setText("Mật khẩu mới không trùng khớp")
                errorChange.visibility=View.VISIBLE
                return
            }
            changePass()

        }
    }

    private fun changePass(){

         accountcrr= mAth.currentUser!!
        databaseReference= FirebaseDatabase.getInstance().getReference("Users")
                if (binding.edPasswdOld.text.toString()!=passwd){
                   binding.errorChange.setText("Mật khẩu cũ không chính xác")
                   binding.errorChange.visibility=View.VISIBLE
                    return
                }else {
                    binding.errorChange.setText("Thành Công")
                    binding.errorChange.visibility=View.VISIBLE
                    databaseReference.child(accountcrr!!.uid).child("passwd")
                        .setValue(binding.edPasswdNew.text.toString())
                    accountcrr.updatePassword(binding.edPasswdNew.text.toString())

                    Toast.makeText(this@ChangePassActivity, "Đổi thành công", Toast.LENGTH_SHORT)
                        .show()
                    binding.apply {
                        edPasswdConfim.setText("")
                        edPasswdNew.setText("")
                        edPasswdOld.setText("")
                        errorChange.visibility=View.GONE
                    }
                }


    }
}