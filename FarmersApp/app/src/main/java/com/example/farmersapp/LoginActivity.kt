package com.example.farmersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.farmersapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference
    private lateinit var pref: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val logUsername = binding.etUsername.text.toString()
            val logPassword = binding.etPassword.text.toString()

            if (logUsername.isEmpty()) {
                binding.etUsername.error = getString(R.string.errorusername)
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(logUsername).matches()) {
                binding.etUsername.error = getString(R.string.emptyusername)
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }
            if (logPassword.isEmpty()) {
                binding.etPassword.error = getString(R.string.errorpass)
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (logPassword.length < 6) {
                binding.etPassword.error = getString(R.string.lenghtpass)
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            loginAccount(logUsername, logPassword)

        }
    }


    private fun loginAccount(logUsername:String,logPassword:String) {
        auth.signInWithEmailAndPassword(logUsername,logPassword).addOnSuccessListener {
            cekUser()
            Toast.makeText(this, "Welcome To FarmerApp", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Email dan Password Invalid", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cekUser() {
        val firebaseUser = auth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("UserLogin")
        ref.child(firebaseUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userType = snapshot.child("UserType").value
                if (userType == "user") {
                    val intent=Intent(applicationContext, halaman_utama::class.java)
                    startActivity(intent)
                }
                if (userType == "admin") {
                    val intent = Intent(applicationContext, TambahBarang::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}