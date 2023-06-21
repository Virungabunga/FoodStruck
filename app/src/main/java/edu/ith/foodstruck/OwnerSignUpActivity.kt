package edu.ith.foodstruck

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

class OwnerSignUpActivity : AppCompatActivity() {
    private lateinit var etSignUpName: EditText
    private lateinit var etSignUpPassword: EditText
    private lateinit var tvAllreadySignedUp: TextView
    private lateinit var firebaseAuth: FirebaseAuth
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

            //Replaced negated "isEmpty" with "isNotEmpty"

            if (userName.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
                if (userPassword.isNotEmpty()) {
                    firebaseAuth.createUserWithEmailAndPassword(userName,userPassword)
                        .addOnSuccessListener {
                            val intent=Intent(this,MainActivity::class.java)
                            startActivity(intent)

                        }

                    //Replaced "setError" with property access syntax

                } else if (userPassword.isEmpty()) {
                    etSignUpPassword.error = "Empty fields are not allowed"

                } else if (userName.isEmpty()) {
                    etSignUpName.error = "Empty fields are not allowed"

                }
            }

        }
    }



