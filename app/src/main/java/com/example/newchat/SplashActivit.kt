package com.example.newchat

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivit : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        if (intent.extras != null) {
            //from notification
            val userId = intent.extras!!.getString("userId")
            FirebaseUtil.allUserCollectionReference().document(userId).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful()) {
                        val model: UserModel = task.getResult().toObject(UserModel::class.java)
                        val mainIntent = Intent(this, MainActivity::class.java)
                        mainIntent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
                        startActivity(mainIntent)
                        val intent = Intent(this, ChatActivity::class.java)
                        AndroidUtil.passUserModelAsIntent(intent, model)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
        } else {
            Handler().postDelayed({
                if (FirebaseUtil.isLoggedIn()) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginPhoneNumberActivity::class.java))
                }
                finish()
            }, 1000)
        }
    }
}