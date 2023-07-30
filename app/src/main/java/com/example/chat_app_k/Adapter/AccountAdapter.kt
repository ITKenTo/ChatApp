package com.example.chat_app_k.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chat_app_k.DetailChatActivity
import com.example.chat_app_k.Model.AccountModel
import com.example.chat_app_k.R

class AccountAdapter(var list: List<AccountModel>, var context: Context):RecyclerView.Adapter<AccountAdapter.AccontViewHolder>() {
    inner class AccontViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_name: TextView = itemView.findViewById(R.id.it_tv_name)
        val tv_email: TextView = itemView.findViewById(R.id.it_tv_email)
        val img_stt: ImageView = itemView.findViewById(R.id.it_tv_tt)
        val img_image: ImageView = itemView.findViewById(R.id.it_img_avt)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccontViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.item_chat,parent,false)
        return AccontViewHolder(view)
    }

    override fun onBindViewHolder(holder: AccontViewHolder, position: Int) {
        holder.tv_name.setText(list[position].fullname)
        holder.tv_email.setText(list[position].email)
        if (list[position].status==1){
            holder.img_stt.setImageResource(R.drawable.period_1)
        }else{
            holder.img_stt.setImageResource(R.drawable.dot)
        }

        if (list[position].image=="null"){
            holder.img_image.setImageResource(R.drawable.avtleeminho)
        }else{
            Glide.with(context).load(list[position].image).into(holder.img_image)
        }

        holder.itemView.setOnClickListener {
            val intent= Intent(context,DetailChatActivity::class.java)
            intent.putExtra("uID",list[position].uid)
            intent.putExtra("username", list[position].fullname)
            context.startActivity(intent)
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    fun fillter(filter: ArrayList<AccountModel>){
        list=filter
        notifyDataSetChanged()
    }

}