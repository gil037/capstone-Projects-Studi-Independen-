package com.example.farmersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.farmersapp.databinding.ActivityLoginBinding
import com.gilang.farmers.cart
import com.gilang.farmers.halaman_utama
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue


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

    private fun loginAccount(logUsername: String, logPassword: String) {
        auth.signInWithEmailAndPassword(logUsername, logPassword)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    val intent = Intent(applicationContext, TambahBarang::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Welcome To FarmerApp", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun cekUsertype() {
        val curentUser = auth.currentUser!!.uid
        val ref = FirebaseDatabase.getInstance()
        ref.getReference().child("UserLogin").child(curentUser).child("TypeUser")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(UserLogin::class.java)
                    val userType = user?.usertype
                    if (userType == 0) {
                        val intent = Intent(applicationContext, halaman_utama::class.java)
                        startActivity(intent)
                    }
                    if (userType == 1) {
                        val intent = Intent(applicationContext, cart::class.java)
                        startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}