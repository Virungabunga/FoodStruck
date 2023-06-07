package edu.ith.foodstruck

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val backgroundImg = findViewById<ImageView>(R.id.ivImage)
        //Typo fix
        val slideAnimation = AnimationUtils.loadAnimation(this,R.anim.slide)

        backgroundImg.startAnimation(slideAnimation)
        Handler().postDelayed({

            startActivity(Intent(this,MainActivity::class.java))
            finish()
        },3000)
    }
}