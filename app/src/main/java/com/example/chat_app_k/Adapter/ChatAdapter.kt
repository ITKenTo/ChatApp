package com.example.chat_app_k.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.Model.ChatModel
import com.example.chat_app_k.R
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatAdapter(var list: List<ChatModel>, var context: Context, var img_url:String):RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private val MESSAGE_RECEIVER=0
    private val MESSAGE_SENDER=1
    var firebaseUser:FirebaseUser?=null

     inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_time_chat: TextView = itemView.findViewById(R.id.tv_time_chat)
         val tv_message_chat: TextView = itemView.findViewById(R.id.tv_message_chat)
        val img_chat: ShapeableImageView = itemView.findViewById(R.id.img_chat_receiver)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        if (viewType==MESSAGE_SENDER) {
            val view= LayoutInflater.from(context).inflate(R.layout.item_sender,parent,false)
            return ChatViewHolder(view)
        }else{
            val view= LayoutInflater.from(context).inflate(R.layout.item_receiver,parent,false)
            return ChatViewHolder(view)
        }

    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat:ChatModel= list[position]
        holder.apply {
            tv_message_chat.setText(chat.message)
            tv_time_chat.setText(chat.time)
        }

        if (img_url=="null") {
            holder.img_chat.setImageResource(R.drawable.avtleeminho)
        }else{
            Glide.with(context).load(img_url).into(holder.img_chat)
        }
        holder.itemView.setOnLongClickListener {
            Delete(chat)

        }
        holder.itemView.setOnClickListener {

        }

    }


    private fun Delete(obj:ChatModel): Boolean {
        val builder= AlertDialog.Builder(context)
        builder.setTitle("Thông báo")
        builder.setMessage("Bạn có chắc chắn muốn xóa tin nhắn?")
        firebaseUser=FirebaseAuth.getInstance().currentUser
        builder.setNegativeButton("Ok"){dialog,which->
            if (obj.senderId==firebaseUser!!.uid) {
                val databaseReference= FirebaseDatabase.getInstance().getReference("Chats")
                databaseReference.child(obj.cid!!).removeValue()
            }else{
                Toast.makeText(context,"Khong thể xóa tin nhắn người gửi",Toast.LENGTH_SHORT).show()
            }
        }
        builder.setPositiveButton("Cancel"){dialog,which->
                 dialog.dismiss()
        }
        val dialog= builder.create()

        dialog.show()

        return true
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser=FirebaseAuth.getInstance().currentUser
        if (list[position].senderId==firebaseUser!!.uid){
            return MESSAGE_SENDER
        }else{
            return MESSAGE_RECEIVER
        }
    }

}