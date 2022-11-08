package edu.ith.foodstruck

import FoodTruck
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text

class UploadMenuActivity : AppCompatActivity() {
    private lateinit var etSignatureName : EditText
    private lateinit var etSignatureDescription : EditText
    private lateinit var etSignaturePrice : EditText
    private lateinit var ivSignature : ImageView

    private lateinit var etFavoriteName :EditText
    private lateinit var etFavoriteDescription : EditText
    private lateinit var etFavoritePrice : EditText
    private lateinit var ivFavorite : ImageView

    private lateinit var etWildName : EditText
    private lateinit var etWildDescription : EditText
    private lateinit var etWildPrice : EditText
    private lateinit var ivWild : ImageView
    private lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    private lateinit var btUploadMenu :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_menu)

        etSignatureName = findViewById(R.id.etSignatureName)
        etSignatureDescription = findViewById(R.id.etSignatureDescription)
        etSignaturePrice = findViewById(R.id.etSignaturePrice)
        etFavoriteName = findViewById(R.id.etFavoriteName)
        etFavoriteDescription = findViewById(R.id.etFavoriteDescription)
        etFavoritePrice =findViewById(R.id.etFavoritePrice)
        etWildName = findViewById(R.id.etWildName)
        etWildDescription = findViewById(R.id.etWildDescription)
        etWildPrice = findViewById(R.id.etWildPrice)
        ivSignature = findViewById(R.id.ivSignatureImage)
        ivFavorite = findViewById(R.id.ivFavoriteImage)
        ivWild = findViewById(R.id.ivWildImage)
        auth = Firebase.auth
        auth.currentUser?.uid
        db = Firebase.firestore
        btUploadMenu = findViewById<Button>(R.id.btUploadMenu)
        buttonsPressed()




    }
    fun buttonsPressed(){
        ivSignature.setOnClickListener{ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()}
        ivFavorite.setOnClickListener{ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()}
        ivWild.setOnClickListener{ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start()}

        btUploadMenu.setOnClickListener{
            val imgURI = btUploadMenu.tag as Uri?
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
            ivSignature.setImageURI(uri)
            ivFavorite.setImageURI(uri)
            ivWild.setImageURI(uri)
            btUploadMenu.setTag(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    fun uploadImage(context: Context, imageFileUri: Uri) {
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
                addMenu(downloadUri)
            } else {
                // Handle failures
                // ...
            }
        }

    }
    fun addMenu(uploadUrl:String) {

        val signatureName = etSignatureName.text.toString()
        val signatureDescription = etSignatureDescription.text.toString()
        val signaturePrice = etSignaturePrice.text.toString().toInt()


        val wildName = etWildName.text.toString()
            val wildDescription = etWildDescription.text.toString()
                val wildPrice = etWildPrice.text.toString().toInt()
        ivWild.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(500, 500)
                .start()
        }

        val favoriteName = etFavoriteName.text.toString()
        val favoriteDescription = etFavoriteDescription.text.toString()
        val favoritePrice = etFavoritePrice.text.toString().toInt()
        ivFavorite.setOnClickListener{
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(500, 500)
                .start()
        }

        val signature = Food(
            signatureName,
            signatureDescription,
            signaturePrice,
            uploadUrl,
            auth.currentUser?.uid

            )
        val favorite = Food (
            favoriteName,
            favoriteDescription,
            favoritePrice,
            uploadUrl,
            auth.currentUser?.uid
                )
        val wild = Food(
            wildName,
            wildDescription,
            wildPrice,
            uploadUrl,
            auth.currentUser?.uid

        )

        db.collection("Signature")
            .add(signature)
            .addOnSuccessListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        db.collection("Wild")
            .add(wild)

        db.collection("favorite")
            .add(favorite)
            .addOnSuccessListener {
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }
    }

}