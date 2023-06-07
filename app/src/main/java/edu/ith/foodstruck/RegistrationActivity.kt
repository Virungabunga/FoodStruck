package edu.ith.foodstruck

//Removed unused imports
import FoodTruck
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

//Removed unused variable "saveCount"
class RegistrationActivity : AppCompatActivity() {
    lateinit var etFoodtruckName: EditText
    lateinit var etFoodTruckBread: EditText
    lateinit var ivUpload: ImageView
    lateinit var db: FirebaseFirestore
    lateinit var buttonSave: Button
    lateinit var btnSelectImage:Button
    lateinit var auth:FirebaseAuth
    lateinit var btPlaceFoodtruck:Button
    private val placeFoodTruck = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
       latLong = it.data?.getParcelableExtra("LAT_LONG",)
        Log.d("RegistrationActivity","${it.data?.getParcelableExtra<LatLng>("LAT_LONG")}")
    }


    private var latLong : LatLng? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btPlaceFoodtruck = findViewById<Button>(R.id.btPlaceFoodtruck)
        etFoodtruckName = findViewById(R.id.etFoodtruckName)
        etFoodTruckBread = findViewById(R.id.etFoodtruckBread)
        db = Firebase.firestore
        ivUpload =findViewById(R.id.ivUpload)
        buttonSave = findViewById<Button>(R.id.buttonSave)
        btnSelectImage = findViewById<Button>(R.id.btnSelectImage)
        auth=Firebase.auth
        buttonsPressed()

    }

    private fun buttonsPressed(){

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
               uploadImage(imgURI)

            }

        }
        btPlaceFoodtruck.setOnClickListener(){
            val intent = Intent(this,PlaceFoodTruckActivity::class.java)
            placeFoodTruck.launch(intent)
        }

    }


    // Added @Deprecated annotation

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Replaced "if" with "when"
         when (resultCode) {
            Activity.RESULT_OK -> {
                //Image Uri will not be null for RESULT_OK
                val uri: Uri = data?.data!!

                // Use Uri object instead of File to avoid storage permissions
                ivUpload.setImageURI(uri)
                buttonSave.tag = uri

            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Removed unused parameter "context

    private fun uploadImage(imageFileUri: Uri) {
        val mStorageRef = FirebaseStorage.getInstance().reference

        val ref = mStorageRef.child("images/${auth.currentUser?.uid}.png")

        val uploadTask = ref.putFile(imageFileUri)
        uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result.toString()
                addFoodTruck(downloadUri)
            } else {
                // Handle failures
            }
        }

    }
    private fun addFoodTruck(uploadUrl:String) {

        //Fixed typos in variable names


        val foodTruckName = etFoodtruckName.text.toString()
        val foodTruckBread = etFoodTruckBread.text.toString()


        val truck = FoodTruck(
            foodTruckName,
            foodTruckBread,
            R.drawable.smallicon, latLong?.longitude,
           latLong?.latitude ,
            userID = auth.currentUser?.uid,
            uploadUrl

        )
        db.collection("FoodTruck")
            .add(truck)
            .addOnSuccessListener {

                val intent = Intent(this, UploadMenuActivity::class.java)
                startActivity(intent)
            }


    }
}
