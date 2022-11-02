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
import android.widget.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
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

    lateinit var db: FirebaseFirestore
    lateinit var buttonSave: Button
    lateinit var btnSelectImage:Button
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        etFoodtruckName = findViewById(R.id.etFoodtruckName)
        etFoodTruckBread = findViewById(R.id.etFoodtruckBread)
        db = Firebase.firestore
        ivUpload =findViewById(R.id.ivUpload)
        buttonSave = findViewById<Button>(R.id.buttonSave)
        btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        auth=Firebase.auth
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

        val uploadTask =
            mStorageRef.child("images/${auth.currentUser?.uid}.png").putFile(imageFileUri)

        uploadTask.addOnSuccessListener {
            mStorageRef.child("images/\${auth.currentUser?.uid}.png)").downloadUrl
                .addOnSuccessListener { uri ->

                    Log.d("!!!", uri.toString())
                    addFoodTruck(uri)

                }

        }
    }



    fun addFoodTruck(uploadUrl:Uri) {

        val foodtruckName = etFoodtruckName.text.toString()
        val foodtruckBread = etFoodTruckBread.text.toString()


        val truck = FoodTruck(
            foodtruckName,
            foodtruckBread,
            R.drawable.smallicon, 59.0,
           19.0 ,
            userID = auth.currentUser?.uid,
            uploadUrl

        )
        db.collection("FoodTruck")
            .add(truck)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }


    }
}
