package com.example.chat_app_k

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app_k.Adapter.ChatAdapter
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.Model.ChatModel
import com.example.chat_app_k.databinding.ActivityDetailChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DateFormat
import java.util.Calendar

class DetailChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailChatBinding
    var firebaseUser:FirebaseUser?=null
    var databaseReference:DatabaseReference?=null
    var listChat= ArrayList<ChatModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_detail_chat)
        binding=ActivityDetailChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().time)

        val intent= getIntent()
        val uID= intent.getStringExtra("uID")

        binding.recycleMessage.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false)

        firebaseUser=FirebaseAuth.getInstance().currentUser

        databaseReference= FirebaseDatabase.getInstance().getReference("Users").child(uID!!)
        binding.apply {
            imgBack.setOnClickListener {
                onBackPressed()
            }

            imgSend.setOnClickListener {
                sendMessage(firebaseUser!!.uid,uID,currentDate)
            }
        }




        databaseReference!!.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user= snapshot.getValue(AccountModel::class.java)
                binding.tvNameChat.setText(user!!.fullname)
                if (user.image=="null"){
                    binding.imgChat.setImageResource(R.drawable.avtleeminho)
                }else{
                    if (!isFinishing) {
                        Glide.with(this@DetailChatActivity).load(user.image).into(binding.imgChat)
                    }
                }
                if (user.status==1){
                    binding.tvSttChat.setText("Online");
                    binding.tvSttChat.setTextColor(getColor(R.color.green))
                }else{
                    binding.tvSttChat.visibility=View.GONE
                }

                getChat(firebaseUser!!.uid, uID, user.image.toString())
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    private fun getChat(senderId:String, receiverId:String, img_url:String){
        val databaseReference:DatabaseReference= FirebaseDatabase.getInstance().getReference("Chats")
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()
                for (dataSnapShot:DataSnapshot in snapshot.children){
                    val chat = dataSnapShot.getValue(ChatModel::class.java)
                    if (chat!!.senderId.equals(senderId) && chat.receiverId.equals(receiverId) ||
                       chat.senderId.equals(receiverId)&& chat.receiverId.equals(senderId)
                            ) {

                        listChat.add(chat)
                    }
                    Log.d("LIST", listChat.size.toString())
                }
                val adapter= ChatAdapter(listChat, this@DetailChatActivity, img_url)
                binding.recycleMessage.adapter=adapter
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
    private fun sendMessage(senderId:String,receiverId:String, currentDate:String){
        var databaseReference= FirebaseDatabase.getInstance().getReference()
        val id: DatabaseReference = databaseReference.push()
        val chatModel= ChatModel(id.key,binding.edMessage.text.toString(),senderId,receiverId,currentDate)

        FirebaseDatabase.getInstance().getReference("Chats").child(id.key.toString())
            .setValue(chatModel).addOnCompleteListener { task->
                if (task.isSuccessful){
                    binding.edMessage.setText("")
                }
            }.addOnFailureListener{
                    e ->
                Toast.makeText(
                    this@DetailChatActivity,
                    e.message.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}