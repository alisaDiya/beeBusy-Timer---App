package com.example.alisadiyapoep2st10037089

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class login : AppCompatActivity() {
    lateinit var ETpass: EditText
    lateinit var ETemail: EditText
    lateinit var btnlogin: Button
    lateinit var tvreg:TextView
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth=FirebaseAuth.getInstance()
        ETpass=findViewById(R.id.ETpass)
        ETemail=findViewById(R.id.ETemail)
        btnlogin=findViewById(R.id.btnlogin)
        tvreg=findViewById(R.id.tvreg)
        auth=FirebaseAuth.getInstance()

        tvreg.setOnClickListener{
            val intent = Intent(this@login, regscreen::class.java)
            startActivity(intent)
        }
        btnlogin.setOnClickListener{

            val email=ETemail.text.toString().trim()
            val password = ETpass.text.toString().trim()
            //checks
            if(email.isEmpty()|| password.isEmpty())
            {
                Toast.makeText(this, "email or password cannot be empty ", Toast.LENGTH_LONG).show()
                return@setOnClickListener

            }
            loginuser(email, password)
        }
    }
    fun loginuser(email:String,password:String)
    {
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) {task ->
            if (task.isSuccessful)
            {
                Toast.makeText(this, "you are logged in ", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@login,landing::class.java)
                startActivity(intent)
            }else
            {
                Toast.makeText(this, "cant be logged in ", Toast.LENGTH_SHORT).show()
            }


        }
    }
}