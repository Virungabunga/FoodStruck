package edu.ith.foodstruck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.set
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.rpc.context.AttributeContext
import kotlin.Error

class OwnerSignUpActivity : AppCompatActivity() {
    private lateinit var etSignUpName: EditText
    private lateinit var etSignUpPassword: EditText
    private lateinit var tvAllreadySignedUp: TextView
    lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_sign_up)

        etSignUpName = findViewById(R.id.etSignUpName)
        etSignUpPassword = findViewById(R.id.etUserSignUpPassword)
        tvAllreadySignedUp = findViewById(R.id.tvallreadyRegistrered)
        firebaseAuth = FirebaseAuth.getInstance()
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)
        
        tvAllreadySignedUp.setOnClickListener(){
            val intent = Intent(this,OwnerLoginActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener() {
            createUser()

        }
    }
        private fun createUser() {
            val userName = etSignUpName.text.toString()
            val userPassword = etSignUpPassword.text.toString()



            if (!userName.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                if (!userPassword.isEmpty()) {
                    firebaseAuth.createUserWithEmailAndPassword(userName,userPassword)
                        .addOnSuccessListener {
                            val intent=Intent(this,MainActivity::class.java)
                            startActivity(intent)

                        }
                } else if (userPassword.isEmpty()) {
                    etSignUpPassword.setError("Empty fields are not allowed")

                } else if (userName.isEmpty()) {
                    etSignUpName.setError("Empty fields are not allowed")

                } else {
                    Log.d("!!!", "nått händer")
                }
            }

        }
    }



