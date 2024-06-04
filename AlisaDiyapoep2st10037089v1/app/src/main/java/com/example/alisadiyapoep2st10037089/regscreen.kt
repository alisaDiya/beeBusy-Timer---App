package com.example.alisadiyapoep2st10037089

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class regscreen : AppCompatActivity() {
    lateinit var edusername: EditText
    lateinit var edemail: EditText
    lateinit var edpassword: EditText
    lateinit var edconfirmpassword: EditText
    lateinit var btnsignup: Button
    lateinit var loginreg:TextView
    lateinit var auth : FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regscreen)
        edusername = findViewById(R.id.edusername)
        edemail = findViewById(R.id.edemail)
        edpassword = findViewById(R.id.edpassword)
        edconfirmpassword = findViewById(R.id.edconfirmpassword)
        btnsignup = findViewById(R.id.btnsignup)
        auth = FirebaseAuth.getInstance()
        loginreg=findViewById(R.id.loginreg)

        btnsignup.setOnClickListener() {
            val name = edusername.text.toString().trim()
            val email = edemail.text.toString().trim()
            val password = edpassword.text.toString().trim()
            val confirmP = edconfirmpassword.text.toString().trim()

            if (name.isEmpty()) {
                edusername.error = "Cannot Be Empty"
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                edemail.error = "Cannot Be Empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                edpassword.error = "Cannot Be Empty"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                edconfirmpassword.error = "Cannot Be Empty"
                return@setOnClickListener
            }
            if (password != confirmP) {
                edconfirmpassword.error = "Has to Match Password"
                return@setOnClickListener
            }
            if (password == confirmP) {
                //Registers User
                registerUser(email, password)
            }
        }
        loginreg.setOnClickListener(){
            val intent = Intent(this@regscreen, login::class.java)
            startActivity(intent)
        }

    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(this, "Authenticate Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@regscreen, login::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this, "Authenticate Failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}

