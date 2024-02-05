package com.example.journalapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    //Firebase Auth
    private  lateinit var  auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)


        binding.registerbtn.setOnClickListener(){
            val intent = Intent(this,SigninActivity::class.java)
            startActivity(intent)
        }
        binding.loginbtn.setOnClickListener {
            loginwithEmailandPassword(
                binding.email.text.toString().trim(),
                binding.password.text.toString().trim(),

            )
        }
        //Auth ref
        auth = Firebase.auth
    }

    private fun loginwithEmailandPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                task ->
                if(task.isSuccessful){
                    //signinsuccess

                   var journal: JournalUser= JournalUser.instance!!
                    journal.userId = auth.currentUser?.uid
                    journal.username=auth.currentUser?.displayName


                    gotojournallist()
                }
                else{
              Toast.makeText(
             this,
             "Authentication is Failed",
              Toast.LENGTH_LONG
              ).show()
                }

            }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            gotojournallist()

        }
    }

    private fun gotojournallist() {
       var intent = Intent(this,Journal_list::class.java)
        startActivity(intent)
    }
}