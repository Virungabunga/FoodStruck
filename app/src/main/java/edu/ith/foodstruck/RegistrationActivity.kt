package edu.ith.foodstruck

import FoodTruck
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
private lateinit var mProgressDialog: ProgressDialog
class RegistrationActivity : AppCompatActivity() {
    lateinit var etFoodtruckName: EditText
    lateinit var etFoodTruckBread: EditText
    lateinit var ivUpload: ImageView
    lateinit var etLatitude: EditText
    lateinit var etLongitude: EditText
    lateinit var db: FirebaseFirestore
    lateinit var buttonSave: Button
    lateinit var btnSelectImage:Button
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etFoodtruckName = findViewById(R.id.etFoodtruckName)
        etFoodTruckBread = findViewById(R.id.etFoodtruckBread)
        etLatitude = findViewById(R.id.etLatitude)
        etLongitude = findViewById(R.id.etLongitude)
        db = Firebase.firestore
        ivUpload =findViewById(R.id.ivUpload)
        buttonSave = findViewById<Button>(R.id.buttonSave)
        btnSelectImage = findViewById<Button>(R.id.btnSelectImage)

        buttonsPressed()

    }

    fun buttonsPressed(){

        btnSelectImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }



        buttonSave.setOnClickListener {
            addFoodTruck()
            val imgURI = buttonSave.tag as Uri?
            if (imgURI == null) {
                Toast.makeText(this, "Please select image first", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage(this, imgURI)
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!

            // Use Uri object instead of File to avoid storage permissions
            ivUpload.setImageURI(uri)
            buttonSave.setTag(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    fun uploadImage(context: Context, imageFileUri: Uri) {
         val mStorageRef = FirebaseStorage.getInstance().reference
        mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Please wait, image being upload")
        mProgressDialog.show()
        val date = Date()
        val uploadTask = mStorageRef.child("posts/${date}.png").putFile(imageFileUri)
        uploadTask.addOnSuccessListener {
            Log.e("Frebase", "Image Upload success")
            mProgressDialog.dismiss()
            val uploadedURL = mStorageRef.child("posts/${date}.png").downloadUrl
            Log.e("Firebase", "Uploaded $uploadedURL")
        }.addOnFailureListener {
            Log.e("Frebase", "Image Upload fail")
            mProgressDialog.dismiss()
        }
    }


    fun addFoodTruck() {

        val foodtruckName = etFoodtruckName.text.toString()
        val foodtruckBread = etFoodTruckBread.text.toString()
        val latitude = etLatitude.text.toString().toDouble()
        val longitude = etLongitude.text.toString().toDouble()

        val truck = FoodTruck(
            foodtruckName,
            foodtruckBread,
            R.drawable.smallicon,
            longitude,
            latitude,
        )
        db.collection("FoodTruck")
            .add(truck)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


    }
}