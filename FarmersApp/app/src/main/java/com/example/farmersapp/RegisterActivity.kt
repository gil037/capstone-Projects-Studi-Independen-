package com.example.farmersapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.util.Patterns.EMAIL_ADDRESS
import android.widget.Toast
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import com.example.farmersapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()

        binding.btnregister.setOnClickListener{
            val regUsername=binding.etRegusername.text.toString()
            val regPassword=binding.etRegpassword.text.toString()
            val reglahir=binding.etreglahir.text.toString()
            binding.tvLogin.setOnClickListener {
                val intent=Intent(this,LoginActivity::class.java)
                startActivity(intent)
            }
//Validasi
            if(regUsername.isEmpty()){
                binding.etRegusername.error=getString(R.string.errorusername)
                binding.etRegusername.requestFocus()
                return@setOnClickListener
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(regUsername).matches()){
                binding.etRegusername.error=getString(R.string.emptyusername)
                binding.etRegusername.requestFocus()
                return@setOnClickListener
            }
            if(regPassword.isEmpty()){
                binding.etRegpassword.error=getString(R.string.errorpass)
                binding.etRegpassword.requestFocus()
                return@setOnClickListener
            }
            if (regPassword.length<6){
                binding.etRegpassword.error=getString(R.string.lenghtpass)
                binding.etRegpassword.requestFocus()
                return@setOnClickListener
            }
            if(reglahir.isEmpty()){
                binding.etreglahir.error=getString(R.string.errorusername)
                binding.etreglahir.requestFocus()
                return@setOnClickListener
            }
         registerAccount(regUsername,regPassword)   
        }
    }

    private fun registerAccount(regUsername: String, regPassword: String) {
        auth.createUserWithEmailAndPassword(regUsername,regPassword)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Toast.makeText(this ,"Register Berhasil",Toast.LENGTH_SHORT).show()
                val intent=Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this,"${it.exception?.message}",Toast.LENGTH_SHORT).show()
                }
            }
    }
}