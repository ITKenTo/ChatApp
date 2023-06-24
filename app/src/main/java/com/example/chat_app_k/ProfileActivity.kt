package com.example.chat_app_k

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.Date
import java.util.UUID

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding:ActivityProfileBinding
    private lateinit var mAth:FirebaseAuth
    private lateinit var user:FirebaseUser
    private lateinit var databaseReference: DatabaseReference

    private var selectedImg: Uri? = null

    private val PICK_IMAGE_REQUEST: Int = 2020

    private lateinit var storage: FirebaseStorage
   // private lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_profile)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAth= FirebaseAuth.getInstance()
        user= mAth.currentUser!!

        storage = FirebaseStorage.getInstance()

         databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(user!!.uid)

        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user= snapshot.getValue(AccountModel::class.java)
                binding.tvFrofileName.setText(user!!.fullname)
                if (user.image=="null"){
                    binding.imgProfile.setImageResource(R.drawable.avtleeminho)
                }else{
                  //  Glide.with(this@ProfileActivity).load(user.image).into(binding.imgProfile)
                    if (!isFinishing) {
                        Glide.with(this@ProfileActivity).load(user.image).into(binding.imgProfile)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.apply {
            back.setOnClickListener {
                onBackPressed()
            }
            imgSelect.setOnClickListener {
              chooseImage()

            }
            btnUpdate.setOnClickListener {
             uploadData()
            }
            linEdit.setOnClickListener {
                startActivity(Intent(this@ProfileActivity,UpadateProfileActivity::class.java))
            }
        }

    }

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent,1)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (data!=null){
           if (data.data!=null) {
               selectedImg=data.data
               binding.imgProfile.setImageURI(selectedImg)
           }
       }
    }

    private fun uploadData(){
        val  reference= storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(selectedImg!!).addOnCompleteListener {
            if (it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener{task->
                    uploaInfor(task.toString())
                }
            }
        }
    }

    private fun uploaInfor(imgurl: String) {

        databaseReference.child("image").setValue(imgurl).addOnSuccessListener {
            Toast.makeText(this,"Thành Công", Toast.LENGTH_SHORT).show()
        }

    }

}