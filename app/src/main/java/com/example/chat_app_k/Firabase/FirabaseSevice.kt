package com.example.chat_app_k.Firabase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.chat_app_k.MainActivity
import com.example.chat_app_k.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FirabaseSevice :FirebaseMessagingService() {

    val CHANNEL_ID = "my_notification_channel"
    companion object{
        var sharedPref: SharedPreferences? = null

        var token:String?
            get(){
                return sharedPref?.getString("token","")
            }
            set(value){
                sharedPref?.edit()?.putString("token",value)?.apply()
            }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        token = p0
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent = Intent(this,MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()
//        var notification1: RemoteMessage.Notification = p0.notification!!
//
//        val title= notification1.title.toString()
//        val mess= notification1.body.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(p0.data["title"])
            .setContentText(p0.data["message"])
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(notificationId,notification)

       // showNotification(p0.data["title"],p0.data["message"])
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){

        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNEL_ID,channelName,IMPORTANCE_HIGH).apply {
            description="MY FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)


    }

}