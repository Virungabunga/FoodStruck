package edu.ith.foodstruck

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class OwnerLoginActivity : AppCompatActivity() {
    lateinit var etUserName : EditText
    lateinit var etUserPassword : EditText
    lateinit var tvNotRegistrered : TextView
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var btShowHide : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_login)
        etUserName=findViewById(R.id.etUserName)
        etUserPassword=findViewById(R.id.etUserPassword)
        tvNotRegistrered=findViewById(R.id.tvNotRegistrered)
        firebaseAuth = FirebaseAuth.getInstance()
        var buttonLogin=findViewById<Button>(R.id.buttonLogin)


        tvNotRegistrered.setOnClickListener(){
            var intent = Intent(this,OwnerSignUpActivity::class.java)
            startActivity(intent)
        }
        buttonLogin.setOnClickListener(){
            loginUser()
        }





    }
    private fun loginUser(){
        val userName = etUserName.text.toString()
        val userPassword = etUserPassword.text.toString()

        if (!userName.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userName).matches()) {
            if (!userPassword.isEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(userName,userPassword)
                    .addOnSuccessListener {
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    }

            } else if (userPassword.isEmpty()) {
                etUserPassword.setError("Empty fields are not allowed")

            } else if (userName.isEmpty()) {
                etUserName.setError("Empty fields are not allowed")

            } else {
                Log.d("!!!", "nått händer")
            }
        }
    }
}