package com.example.alisadiyapoep2st10037089

import android.icu.util.LocaleData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.*

class goal : AppCompatActivity() {
    //variables
    lateinit var auth: FirebaseAuth
    lateinit var btnSave:Button
    lateinit var btnClear:Button
    lateinit var etMinHours:EditText
    lateinit var etMaxHours:EditText
    lateinit var tvgoals:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goal)
        //links to firebase
        auth = Firebase.auth
        btnClear=findViewById(R.id.btnClear)
        btnSave=findViewById(R.id.btnSave)
        etMinHours=findViewById(R.id.etMinHours)
        tvgoals=findViewById(R.id.tvgoals)
        etMaxHours=findViewById(R.id.etMaxHours)

        //saves users max and min goal
        btnSave.setOnClickListener {
            val minHours = etMinHours.text.toString().toIntOrNull()
            val maxHours = etMaxHours.text.toString().toIntOrNull()


            if (minHours != null && maxHours != null) {
                // Update UI to display the goals for the day
                displayGoals(minHours, maxHours)


            } else
            {
                Toast.makeText(this, "Please input some values", Toast.LENGTH_SHORT).show()
        }
        }
        //clear information
        btnClear.setOnClickListener {
            clearGoals()
        }
    }


    //displays the users input
    private fun displayGoals(minHours: Int, maxHours: Int) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd") // Define your desired date format
        val currentDate = dateFormat.format(Date())
        tvgoals.text = "Your goal for $currentDate:\nMinimum Hours: $minHours\nMaximum Hours: $maxHours"


    }

    private fun clearGoals() {
      //clears users input
        tvgoals.text = ""
    }
}
