package com.example.alisadiyapoep2st10037089

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar

class MainActivity : AppCompatActivity() {
    lateinit var IVlogo1: ImageView
    lateinit var progressBar:ProgressBar
    val delay: Long =5000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
       IVlogo1=findViewById(R.id.IVlogo1)
        progressBar=findViewById(R.id.progressBar)

        //event handler - to loop the splashy

        Handler(Looper.getMainLooper()).postDelayed({
          progressBar.visibility=View.VISIBLE
            progressBar.visibility=View.GONE
            //start the next activity

            val intent = Intent(this@MainActivity,Homescreen::class.java)
            startActivity(intent)
            finish()
        },delay)

    }
}
