package com.example.chat_app_k.Model

import android.provider.ContactsContract.CommonDataKinds.Email

data class AccountModel (
    val uid:String?=null,
    val email:String?=null,
    val fullname:String?=null,
    val passwd:String?=null,
    val image:String?=null,
    val status:Int?=1
)