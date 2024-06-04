package com.example.alisadiyapoep2st10037089

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class timer : AppCompatActivity() {
    lateinit var spinner: Spinner
    lateinit var capButton: Button
    lateinit var edName: EditText
    lateinit var edDesc: EditText
    lateinit var startDateBtn: Button
    lateinit var startTimeBtn: Button
    lateinit var endDateBtn: Button
    lateinit var endTimeBtn: Button
    lateinit var pic: Button

    lateinit var btnaddcat :Button
    lateinit var editTextTextPersonName2:EditText
    lateinit var array : ArrayList<String>
    lateinit var adapter : ArrayAdapter<String>

    lateinit var database: DatabaseReference
    lateinit var btnRead: Button

    //globals
    var startDate: Date? = null
    var startTime: Date? = null
    var endDate: Date? = null
    var endTime: Date? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        edName = findViewById(R.id.edname)
        editTextTextPersonName2=findViewById(R.id.editTextTextPersonName2)
        edDesc = findViewById(R.id.edDes)
        spinner = findViewById(R.id.spinner)
        startDateBtn = findViewById(R.id.startDateBtn)
        startTimeBtn = findViewById(R.id.startTimeBtn)
        endDateBtn = findViewById(R.id.endDateBtn)
        endTimeBtn = findViewById(R.id.endTimeBtn)
        capButton = findViewById(R.id.capButton)
        pic = findViewById(R.id.buttonpic)

        btnaddcat=findViewById(R.id.btnaddcat)
        btnRead = findViewById(R.id.btnRead)



        database = FirebaseDatabase.getInstance().reference




        //populate the spinner
        array = ArrayList()
        //categories.add("")
        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, array)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //View

        //Add category to spinner
        btnaddcat.setOnClickListener{
            val newCategory = editTextTextPersonName2.text.toString()
            if(newCategory.isNotEmpty() && !array.contains(newCategory)){
                array.add(newCategory)
                adapter.notifyDataSetChanged()

            }
        }

        startDateBtn.setOnClickListener { showDate((startDateListener)) }
        endDateBtn.setOnClickListener { showDate(endDateListener) }
        startTimeBtn.setOnClickListener { showTimePicker((startTimeListener)) }
        endTimeBtn.setOnClickListener { showTimePicker((endTimeListener)) }
        capButton.setOnClickListener {
            val selectedItem = spinner.selectedItem as String
            val taskName = edName.text.toString()
            val taskDesc = edDesc.text.toString()
            val taskCategory=editTextTextPersonName2.text.toString()


            if (taskName.isEmpty()) {
                edName.error = "Please enter a name"
                return@setOnClickListener

            }
            if (taskDesc.isEmpty()) {
                edDesc.error = "please enter a description"
                return@setOnClickListener
            }
            if (taskCategory.isEmpty())
            {
                editTextTextPersonName2.error="please enter a category"
                return@setOnClickListener
            }
            saveToFirebase(selectedItem, taskName, taskDesc,taskCategory)
        }
        pic.setOnClickListener {
            val intent = Intent(this, camera::class.java)
            startActivity(intent)
        }

// calling the view records method
        btnRead.setOnClickListener()
        {
            fetchAndDisplay()
        }
    }

    //method to pick the date
    fun showDate(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val currentDate = Calendar.getInstance()
        val year = currentDate.get(Calendar.YEAR)
        val month = currentDate.get(Calendar.MONTH)
        val day = currentDate.get(Calendar.DAY_OF_MONTH)

        // Create a DatePickerDialog
        val datePickerDialog = DatePickerDialog(
            this,
            dateSetListener, year, month, day
        )

        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    fun showTimePicker(timeSetListener: TimePickerDialog.OnTimeSetListener) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(
            this,
            timeSetListener, hour, minute, true
        )
        timePickerDialog.show()
    }

    //format --> fb --> date --> date util
    val startDateListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, day)
            startDate = selectedCalendar.time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDateString = dateFormat.format(startDate!!)
            startDateBtn.text = selectedDateString


        }


    val startTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        startTime = selectedCalendar.time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTimeString = timeFormat.format(startTime!!)
        startTimeBtn.text = selectedTimeString
    }

    //end date
    val endDateListener =
        DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
            val selectedCalendar = Calendar.getInstance()
            selectedCalendar.set(year, month, day)
            endDate = selectedCalendar.time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val selectedDateString = dateFormat.format(endDate!!)

            endDateBtn.text = selectedDateString
        }

    //end time listener
    val endTimeListener = TimePickerDialog.OnTimeSetListener { _: TimePicker, hourOfDay, minute ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        endTime = selectedCalendar.time

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val selectedTimeString = timeFormat.format(endTime!!)
        endTimeBtn.text = selectedTimeString

    }

    //method for firebase
    fun saveToFirebase(item: String, taskName: String, taskDesc: String,taskCategory: String) {
        //formats
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        //fectch the value from the loacl btns text
        val startDateString = startDateBtn.text.toString()
        val startTimeString = startTimeBtn.text.toString()
        val taskCategory=editTextTextPersonName2.text.toString()
        val endDateString = endDateBtn.text.toString()
        val endTimeString = endTimeBtn.text.toString()
        //parse values for firebase
        val startDate = dateFormat.parse(startDateString)
        val startTime = timeFormat.parse(startTimeString)
        val endDate = dateFormat.parse(endDateString)
        val endTime = timeFormat.parse(endTimeString)

        //cals
        val totalTimeInMillis = endDate.time - startDate.time + endTime.time - startTime.time
        val totalMinutes = totalTimeInMillis / (1000 * 60)
        val totalHours = totalMinutes / 60
        val minutesRemaining = totalMinutes % 60
        val totalTimeString = String.format(
            Locale.getDefault(),
            "%02d:%02d", totalHours, minutesRemaining
        )

        val key = database.child("items").push().key
        if (key != null) {
            val task = TaskModel(
                taskName, taskDesc,taskCategory, startDateString, startTimeString, endDateString, endTimeString,totalTimeString
            )
            database.child("items").child(key).setValue(task)
                .addOnSuccessListener {
                    Toast.makeText(this, "timesheet entry saved to database", Toast.LENGTH_SHORT)
                        .show()
                }
                .addOnFailureListener { err ->
                    Toast.makeText(this, "Error: ${err.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    //method to view items from the db
    fun fetchAndDisplay() {
        database.child("items").get()
            .addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val records = ArrayList<String>()
                    dataSnapshot.children.forEach { snapshot ->
                        val task = snapshot.getValue(TaskModel::class.java)
                        task?.let {
                            records.add(
                                " Name ${it.taskName} \n" +
                                        "Desc ${it.taskDesc} :\n" +
                                        "Category: ${it.taskCategory}\n" +

                                        "Start Date: ${it.startDateString}\n" +
                                        "Start Time: ${it.startTimeString}\n" +

                                        "End Date: ${it.endDateString}\n" +

                                        "End Time: ${it.endTimeString}\n" +

                                        "Total hours worked for category: ${it.totalTimeString}\n"
                            )
                        }
                    }
                    displayDialog(records)
                } else {
                    Toast.makeText(this, "No records found", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "failed to fetch the data", Toast.LENGTH_SHORT).show()
            }
    }

    fun displayDialog(records:ArrayList<String>)
    {
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Database records")
        val arrayAdapter=ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1,records)
        builder.setAdapter(arrayAdapter,null)
        builder.setPositiveButton("okay",null)
        builder.show()
    }

}
//class ends
data class TaskModel(
    var taskName : String? = null,
    var taskDesc : String? = null,
    var taskCategory: String? = null,
    var startDateString : String? = null,
    var startTimeString : String? = null,
    var endDateString : String? = null,
    var endTimeString : String? = null,
    var totalTimeString : String? = null,


    )