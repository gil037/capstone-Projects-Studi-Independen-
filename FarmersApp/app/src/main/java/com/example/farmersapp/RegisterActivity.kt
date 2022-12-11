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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth:FirebaseAuth
    private lateinit var reff:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        reff=FirebaseDatabase.getInstance().getReference("UserLogin")

        binding.btnregister.setOnClickListener{
            val regUsername=binding.etRegusername.text.toString()
            val regPassword=binding.etRegpassword.text.toString()
            val regPhone=binding.etregphone.text.toString()
            val regName=binding.etName.text.toString()
            val avatar=""
            binding.tvLogin.setOnClickListener {
                val intent=Intent(this,ProfileActivity::class.java)
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
            if(regPhone.isEmpty()){
                binding.etregphone.error=getString(R.string.errorusername)
                binding.etregphone.requestFocus()
                return@setOnClickListener
            }
            if(regName.isEmpty()){
                binding.etregphone.error=getString(R.string.errorusername)
                binding.etregphone.requestFocus()
                return@setOnClickListener
            }
         registerAccount(regUsername,regPassword,regPhone,regName,avatar)
        }
    }
    private fun registerAccount(regUsername: String, regPassword: String,nama:String,phone:String,avatar: String) {
        auth.createUserWithEmailAndPassword(regUsername,regPassword)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    saveDatalogin(regUsername,nama,phone, avatar,"user")
                    Toast.makeText(this ,"Register Berhasil",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,"${it.exception?.message}",Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun saveDatalogin(regUsername:String,phone:String,nama:String,avatar:String,usertype:String){
        val curentUser=auth.currentUser!!.uid
        val userMap= HashMap<String,Any>()
        userMap["Id"]=curentUser
        userMap["Nama"]=nama
        userMap["Email"]=regUsername
        userMap["Phone"]=phone
        userMap["Avatar"]=avatar
        userMap["UserType"]=usertype
        reff.child(curentUser).setValue(userMap).addOnCompleteListener{
            if (it.isSuccessful){
                Toast.makeText(applicationContext,"Success",Toast.LENGTH_SHORT).show()
                val intent=Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }else{
                Toast.makeText(this,"${it.exception?.message}",Toast.LENGTH_SHORT).show()

            }
        }
    }
}