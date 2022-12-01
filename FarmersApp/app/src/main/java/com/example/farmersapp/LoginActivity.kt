package com.example.farmersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.farmersapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener{
            val intent= Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val logUsername=binding.etUsername.text.toString()
            val logPassword=binding.etPassword.text.toString()

            if(logUsername.isEmpty()){
                binding.etUsername.error=getString(R.string.errorusername)
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(logUsername).matches()){
                binding.etUsername.error=getString(R.string.emptyusername)
                binding.etUsername.requestFocus()
                return@setOnClickListener
            }
            if(logPassword.isEmpty()){
                binding.etPassword.error=getString(R.string.errorpass)
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (logPassword.length<6){
                binding.etPassword.error=getString(R.string.lenghtpass)
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            loginAccount(logUsername,logPassword)
        }
    }

    private fun loginAccount(logUsername: String, logPassword: String) {
        auth.signInWithEmailAndPassword(logUsername,logPassword)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this ,"Welcome To FarmerApp", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}