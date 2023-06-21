package com.example.chat_app_k

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chat_app_k.Adapter.AccountAdapter
import com.example.chat_app_k.Login_Register.LoginActivity
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var mAth:FirebaseAuth
    var listAccount= ArrayList<AccountModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAth=Firebase.auth
       // setContentView(R.layout.activity_main)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclePeople.layoutManager= LinearLayoutManager(this,RecyclerView.VERTICAL, false)

        getUserList()
//        adapter= AccountAdapter(listAccount,this)
//        binding.recyclePeople.adapter=adapter
        Log.d("SIZE", listAccount.toString())
        //adapter.notifyDataSetChanged()
        binding.option.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.option)
            popupMenu.menuInflater.inflate(R.menu.menu_item, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.profile -> {
                        // Xử lý khi nhấp vào menu item 1
                        startActivity(Intent(this,ProfileActivity::class.java))
                        true
                    }
                    R.id.logout -> {
                        // Xử lý khi nhấp vào menu item 2
                        var databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")
                        val user:FirebaseUser? = mAth.currentUser
                        if (user != null) {
                            databaseReference.child(user.uid).child("status").setValue(0)
                        }
                        mAth.signOut()
                        startActivity(Intent(this,LoginActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
        }

    }
    private fun getUserList(){

        var databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")

        val user:FirebaseUser? = mAth.currentUser
        databaseReference.addValueEventListener(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                listAccount.clear()
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val account = dataSnapShot.getValue(AccountModel::class.java)


                   if (!account!!.uid.equals(user!!.uid)) {
                        listAccount.add(account)
                  }
                }
                val adapter= AccountAdapter(listAccount,this@MainActivity)
                binding.recyclePeople.adapter=adapter
                adapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

//    override fun onde() {
//        super.onPause()
//        var databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")
//        val user:FirebaseUser? = mAth.currentUser
//
//        if (user != null) {
//            databaseReference.child(user.uid).child("status").setValue(0)
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
        var databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")
        val user:FirebaseUser? = mAth.currentUser

        if (user != null) {
            databaseReference.child(user.uid).child("status").setValue(0)
        }
    }

//    override fun onStop() {
//        super.onStop()
//        var databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")
//        val user:FirebaseUser? = mAth.currentUser
//
//        if (user != null) {
//            databaseReference.child(user.uid).child("status").setValue(0)
//        }
//    }
    override fun onResume() {
        super.onResume()
        var databaseReference:DatabaseReference =FirebaseDatabase.getInstance().getReference("Users")
        val user:FirebaseUser? = mAth.currentUser

        if (user != null) {
            databaseReference.child(user.uid).child("status").setValue(1)
        }
    }
}