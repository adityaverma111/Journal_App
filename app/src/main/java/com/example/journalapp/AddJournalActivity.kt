package com.example.journalapp

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.journalapp.databinding.ActivityAddJournalBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.Date



class AddJournalActivity : AppCompatActivity() {


    lateinit var  binding:ActivityAddJournalBinding


    //Credentials
    var currentsUserId:String = " "
    var currentUserName: String = " "

    //firebase
    lateinit var auth:FirebaseAuth
    lateinit var user:FirebaseUser

    //Firebase firestore
    var db : FirebaseFirestore= FirebaseFirestore.getInstance()
    lateinit var  storageReference: StorageReference
    var collectionRefresher: CollectionReference = db.collection("Journal")
    lateinit var imageUri: Uri


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_journal)

        storageReference = FirebaseStorage.getInstance().getReference()
        auth = Firebase.auth

        binding.apply {
            postProgressBar.visibility = View.INVISIBLE

            if (JournalUser.instance != null) {
               // currentsUserId = JournalUser.instance!!.userId.toString()
               currentsUserId = auth.currentUser?.uid.toString()
               currentUserName = auth.currentUser?.displayName.toString()


                postUsernameTextView.text = currentUserName

            }
            //Getting Image from the gallery
            postCameraButton.setOnClickListener(){
                var i : Intent=Intent(Intent.ACTION_GET_CONTENT)
                i.setType("image/*")
                startActivityForResult(i,1)

            }
            postSaveJournalButton.setOnClickListener() {
                saveJournal()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveJournal() {
        val title: String = binding.postTitleEt.text.toString().trim()
        val thoughts: String = binding.postDescriptionEt.text.toString().trim()

        binding.postProgressBar.visibility = View.VISIBLE

        if (title.isNotEmpty() && thoughts.isNotEmpty() && imageUri != null) {
            val filepath: StorageReference = storageReference.child("journal_images")
                .child("my_image_${System.currentTimeMillis() / 1000}")

            filepath.putFile(imageUri!!).addOnSuccessListener {
                filepath.downloadUrl.addOnSuccessListener { uri ->
                    val imageUri = uri.toString()
                    val timestamp = Timestamp(Date())

                    val journal = Journal(title, thoughts, imageUri, currentsUserId, timestamp, currentUserName)

                    val databaseRef = FirebaseDatabase.getInstance().getReference("Journals")
                    val journalId = databaseRef.push().key

                    journalId?.let {
                        databaseRef.child(it).setValue(journal)
                            .addOnSuccessListener {
                                binding.postProgressBar.visibility = View.INVISIBLE
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener { e ->
                                binding.postProgressBar.visibility = View.INVISIBLE
                                Log.e("FirebaseError", "Failed to write data", e)
                            }
                    } ?: Log.e("FirebaseError", "Failed to generate a unique key for the journal entry")
                }.addOnFailureListener { e ->
                    binding.postProgressBar.visibility = View.INVISIBLE
                    Log.e("FirebaseError", "Failed to upload image", e)
                }
            }
        } else {
            binding.postProgressBar.visibility = View.INVISIBLE
            // Handle empty fields
        }
    }



    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data != null){
                imageUri = data.data!!
                binding.postImageView.setImageURI(imageUri)//showig the image
            }
        }
    }

    override fun onStart() {
        super.onStart()
        user = auth.currentUser!!

    }

    override fun onStop() {
        super.onStop()
        if(auth != null){

        }
    }
}




