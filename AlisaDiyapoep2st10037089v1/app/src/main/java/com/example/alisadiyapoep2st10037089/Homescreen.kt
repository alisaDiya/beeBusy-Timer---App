package com.example.alisadiyapoep2st10037089

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Homescreen : AppCompatActivity() {
    lateinit var imageView2: ImageView
    lateinit var TVsolgan1: TextView
    lateinit var TVabout1: TextView
    lateinit var btnlgs: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)

        imageView2=findViewById(R.id.imageView2)
        TVsolgan1=findViewById(R.id.TVsolgan1)
        TVabout1=findViewById(R.id.TVabout1)
        btnlgs=findViewById(R.id.btnlgs)
        btnlgs.setOnClickListener()
        {
            tonext()
        }
    }
    private fun tonext()
    {
        val intent = Intent(this@Homescreen,regscreen::class.java)
        startActivity(intent)
    }
}