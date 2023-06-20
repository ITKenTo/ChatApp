package com.example.chat_app_k.Model

data class ChatModel(
    val cID:String?="",
    val message:String?="",
    val senderId:String?="",
    val receiverId:String?="",
    val time:String?=""
) {
}