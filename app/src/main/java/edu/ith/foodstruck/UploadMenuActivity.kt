package edu.ith.foodstruck

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

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
    private lateinit var btUploadDish1 :Button
    private lateinit var btUploadDish2 :Button
    private lateinit var btUploadDish3 :Button

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

        db = Firebase.firestore
        btUploadDish1 = findViewById<Button>(R.id.btUploadPic1)
        btUploadDish2 = findViewById<Button>(R.id.btUploadPic2)
        btUploadDish3 = findViewById<Button>(R.id.btUploadPic3)
        buttonsPressed()





    }
    fun buttonsPressed(){
        ivSignature.setOnClickListener{ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(333)
                }
        ivFavorite.setOnClickListener{ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(222)}
        ivWild.setOnClickListener{ImagePicker.with(this)
            .crop()
            .compress(1024)
            .maxResultSize(1080, 1080)
            .start(111)}
            //Sig
        btUploadDish1.setOnClickListener{
            val imgURI = btUploadDish1.tag as Uri?
            if (imgURI == null) {
                Toast.makeText(this, "Please select image first", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage(this, imgURI)
            }
        }       //Favori
        btUploadDish2.setOnClickListener{
            val imgURI = btUploadDish2.tag as Uri?
            if (imgURI == null) {
                Toast.makeText(this, "Please select image first", Toast.LENGTH_SHORT).show()
            } else {
                uploadImage(this, imgURI)
            }
        }       //Wild
        btUploadDish3.setOnClickListener{
            val imgURI = btUploadDish3.tag as Uri?
            if (imgURI == null) {
                Toast.makeText(this, "Please select image first", Toast.LENGTH_SHORT).show()
            } else {

                uploadImage(this, imgURI)
            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            val uri: Uri = data?.data!!
//            btUploadDish1.setTag(uri)
//            ivWild.setImageURI(uri)

            Log.d("!!!", requestCode.toString())

             when (requestCode){
                111 -> {btUploadDish1.setTag(uri)
                      ivWild.setImageURI(uri)}


                222 ->  { btUploadDish2.setTag(uri)
                       ivFavorite.setImageURI(uri) }

                333 ->  { btUploadDish3.setTag(uri)
                       ivSignature.setImageURI(uri)}
                 else  -> return
            }
            // Use Uri object instead of File to avoid storage permissions






        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    fun uploadImage(context: Context, imageFileUri: Uri) {
        val mStorageRef = FirebaseStorage.getInstance().reference
        val uri1 = btUploadDish1.getTag()
        val uri2 = btUploadDish2.getTag()
        val uri3 = btUploadDish3.getTag()

        val ref1 = mStorageRef.child("images/${auth.currentUser?.uid}/dish1png")
        val ref2 = mStorageRef.child("images/${auth.currentUser?.uid}/dish2.png")
        val ref3 = mStorageRef.child("images/${auth.currentUser?.uid}/dish3.png")

        if(uri1 != null && uri2!=null && uri3!=null){

            val uploadTask1 = ref1.putFile(uri1 as Uri)

            uploadTask1.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref1.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri1 = task.result.toString()

                    val uploadTask2 = ref2.putFile(uri2 as Uri)

                    uploadTask2.continueWithTask { task ->
                        if (!task.isSuccessful) {
                            task.exception?.let {
                                throw it
                            }
                        }
                        ref2.downloadUrl
                    }.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            val downloadUri2 = task.result.toString()
                            val uploadTask = ref3.putFile(uri3 as Uri)

                            uploadTask.continueWithTask { task ->
                                if (!task.isSuccessful) {
                                    task.exception?.let {
                                        throw it
                                    }
                                }
                                ref3.downloadUrl
                            }.addOnCompleteListener { task ->
                                if (task.isSuccessful) {

                                    val downloadUri3 = task.result.toString()
                                    addMenu(downloadUri1,downloadUri2,downloadUri3)
                                } else {
                                    // Handle failures
                                    // ...
                                }
                            }
                        } else {
                            // Handle failures
                            // ...
                        }
                    }
                } else {
                    // Handle failures
                    // ...
                }
            }

        }
//        val uploadTask = ref.putFile(imageFileUri)
//        uploadTask.continueWithTask { task ->
//            if (!task.isSuccessful) {
//                task.exception?.let {
//                    throw it
//                }
//            }
//            ref.downloadUrl
//        }.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val downloadUri = task.result.toString()
//                addMenu(downloadUri)
//            } else {
//                // Handle failures
//                // ...
//            }
//        }

    }
    fun addMenu(uploadUrl1:String,uploadUrl2:String,uploadUrl3:String) {

        val userId = auth.currentUser?.uid

        val signatureName = etSignatureName.text.toString()
        val signatureDescription = etSignatureDescription.text.toString()
        val signaturePrice = etSignaturePrice.text.toString().toInt()


        val wildName = etWildName.text.toString()
        val wildDescription = etWildDescription.text.toString()
        val wildPrice = etWildPrice.text.toString().toInt()



        val favoriteName = etFavoriteName.text.toString()
        val favoriteDescription = etFavoriteDescription.text.toString()
        val favoritePrice = etFavoritePrice.text.toString().toInt()

        val signature = Food(
            signatureName,
            signatureDescription,
            signaturePrice,
            uploadUrl3,
            auth.currentUser?.uid

            )
        val favorite = Food (
            favoriteName,
            favoriteDescription,
            favoritePrice,
            uploadUrl2,
            auth.currentUser?.uid
                )
        val wild = Food(
            wildName,
            wildDescription,
            wildPrice,
            uploadUrl1,
            auth.currentUser?.uid

        )
        if (userId != null) {
            db.collection("users").document(userId).collection("Menu")
                .add(signature)

        }
        if (userId != null) {
            db.collection("users").document(userId).collection("Menu")
                .add(wild)
        }

        if (userId != null) {
            db.collection("users").document(userId).collection("Menu")
                .add(favorite)

        }

    }

}