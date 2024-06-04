

package com.example.alisadiyapoep2st10037089

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*

class camera : AppCompatActivity() {
    // varaiables
    lateinit var imageViewCam2: ImageView
    lateinit var btnChoose: Button
    lateinit var btnTakePic: Button
    lateinit var btnUpload: Button

    //globals
    var filePath: Uri? = null
    val PICK_IMAGE_REQUEST = 22
    val storage = FirebaseStorage.getInstance()
    val storageReference = storage.reference
    val REQUEST_IMAGE_CAPTURE = 1
    val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        //intailising variables
        imageViewCam2 = findViewById(R.id.imageViewCam2)
        btnChoose = findViewById(R.id.btnCam2Choose)
        btnTakePic = findViewById(R.id.btnCam2TakePic)
        btnUpload = findViewById(R.id.btnCam2Upload)

        //Allows the user to select an image from their gallery
        btnChoose.setOnClickListener {
            selectImage()
        }

        //allows the user to take a picture using their camera
        btnTakePic.setOnClickListener {

            dispatchTakeePictureIntent()
        }
        //button allows the user to upload their choose picture to firebase
        btnUpload.setOnClickListener {
            uploadImage()
        }



    }

    //privacy method
    fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "select image"),
            PICK_IMAGE_REQUEST
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data
            != null && data.data != null
        ) {
            filePath = data.data


            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filePath)
                imageViewCam2.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==
            Activity.RESULT_OK
        ) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageViewCam2.setImageBitmap(imageBitmap)

            // SAVE IMAGE TO FIRE
            saveImageToFirebase(imageBitmap)
        }


    }

    fun dispatchTakeePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }

        }
    }

    //method to save the url
    fun saveImageURLToFirestore(imageUrl: String) {
        val imageMap = hashMapOf("imageUrl" to imageUrl)

        firestore.collection("images")
            .add(imageMap)
            .addOnSuccessListener {
                Toast.makeText(this, "saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed to save image", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageToFirebase(imageBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageName = UUID.randomUUID().toString() + ".jpg"
        val imageRef = storageReference.child("image/$imageName")
        imageRef.putBytes(data)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageURL = uri.toString()
                    saveImageURLToFirestore(imageURL)

                }.addOnFailureListener {
                    Toast.makeText(this, "an error occurred", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(this, "an error occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun uploadImage(){
        filePath?.let { filePath ->
            if (contentResolver.openInputStream(filePath) != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()


                val ref = storageReference.child("images/${UUID.randomUUID()}.jpg")
                ref.putFile(filePath)
                    .addOnSuccessListener { takeSnapShot ->
                        progressDialog.dismiss()
                        Toast.makeText(this, "message uploaded", Toast.LENGTH_SHORT).show()
                        //get the downloaded url from firestore
                        ref.downloadUrl.addOnSuccessListener { uri ->
                            val imageURL = uri.toString()
                            //save to firestore
                            saveImageURLToFirestore(imageURL)

                        }.addOnFailureListener { e ->
                            Toast.makeText(this, "failed to get url", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "failed to get url", Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapShot ->
                        val progress =
                            (100.0 * taskSnapShot.bytesTransferred / taskSnapShot.totalByteCount)
                        progressDialog.setMessage("Uploaded: ${progress.toInt()} %")
                    }
            } else {
                Toast.makeText(this, "file doesnt exist", Toast.LENGTH_SHORT).show()
            }
        }?: run{
            Toast.makeText(this, "no image selected", Toast.LENGTH_SHORT).show()
        }
    }
}
