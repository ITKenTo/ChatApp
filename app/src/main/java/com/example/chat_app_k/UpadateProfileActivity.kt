package com.example.chat_app_k

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.databinding.ActivityUpadateProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UpadateProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityUpadateProfileBinding

    private lateinit var mAth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private lateinit  var  databaseReference:DatabaseReference
    private var passwd: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_upadate_profile)
        binding=ActivityUpadateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAth= FirebaseAuth.getInstance()
        Profile()
        binding.apply {

            back.setOnClickListener {
                onBackPressed()
            }
            btnUpdate.setOnClickListener {
                validate()
                Update()
            }

            editPass.setOnClickListener {
                val intent:Intent= Intent(this@UpadateProfileActivity, ChangePassActivity::class.java)
                intent.putExtra("Passwd", passwd)
                startActivity(intent)
            }

        }
    }

    private fun Profile(){
        user= mAth.currentUser!!
         databaseReference= FirebaseDatabase.getInstance().getReference("Users")
        databaseReference.child(user!!.uid).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val us= snapshot.getValue(AccountModel::class.java)
                passwd= us!!.passwd.toString()
                binding.apply {
                    edEmailEdit.setText(us!!.email)
                    edFullnameEdit.setText(us!!.fullname)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun Update(){
        binding.apply {
            databaseReference.child(user.uid).child("email").setValue(edEmailEdit.text.toString())
            user.updateEmail(edEmailEdit.text.toString())
            databaseReference.child(user.uid).child("fullname").setValue(edFullnameEdit.text.toString())
        }

    }
    private fun validate(){
        binding.apply {
            if (edEmailEdit.text.toString().isEmpty()) {
                error.visibility= View.VISIBLE
                error.setText("Không để trống Email")
                return
            }
            if (edFullnameEdit.text.toString().isEmpty()) {
                error.setText("Không để trống Họ tên")
                error.visibility= View.VISIBLE
                return
            }
            error.visibility= View.GONE
//            if (edPasswdRes.text.toString().isEmpty()) {
//                error.setText("Không để trống Mật Khẩu")
//                return
//            }
        }
    }
}