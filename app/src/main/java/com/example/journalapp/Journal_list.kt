package com.example.journalapp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.journalapp.databinding.ActivityJournalListBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import java.sql.Timestamp
import kotlin.collections.ArrayList

class Journal_list : AppCompatActivity() {


    //retrive data
    lateinit var dbb: DatabaseReference
    private lateinit var itemrecyclerview: RecyclerView
    private lateinit var itemarraylist: ArrayList<Journal>
    lateinit var binding: ActivityJournalListBinding
       lateinit var journalList: MutableList<Journal>
      lateinit var adapter: JournalRecyclerAdapter
     var eventListener:ValueEventListener? = null


    //Firebase Refrences

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var user: FirebaseUser
     var db = FirebaseFirestore.getInstance()

      lateinit var storageReference: StorageReference


      var collectionReference: CollectionReference = db.collection("Journals")


    lateinit var nopostTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_journal_list)
        itemrecyclerview = findViewById(R.id.recyclerView)
        itemrecyclerview.layoutManager = LinearLayoutManager(this)
        itemrecyclerview.hasFixedSize()
        // itemarraylist = arrayListOf<Journal>()
        //  getitemData()


        //Firebase Auth
        firebaseAuth = Firebase.auth
        user = firebaseAuth.currentUser!!


        //Recyclerview
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(this)

        //Post araylist
        journalList = arrayListOf<Journal>()


    }

    /* private fun getitemData() {

      dbb = FirebaseDatabase.getInstance().getReference("Journals")
        dbb.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for(itmsnapshot in snapshot.children){
                       val item =  itmsnapshot.getValue(Journal::class.java)
                        itemarraylist.add(item!!)
                    }
                    itemrecyclerview.adapter = JournalRecyclerAdapter(itemarraylist)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }*/

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add -> if (user != null && firebaseAuth != null) {
                val intent = Intent(this, AddJournalActivity::class.java)
                startActivity(intent)
            }

            R.id.action_signout -> {
                if (user != null && firebaseAuth != null) {
                    firebaseAuth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Getting all post


    override fun onStart(){
        super.onStart()

        collectionReference.whereEqualTo("userId",
            user.uid)
            .get()
            .addOnSuccessListener {
                Log.i("TAGY","sizey:${it.size()}")
                if (!it.isEmpty) {
                     Log.i("TAGY","Elements: ${it}")


                    for(document in it){
                        var journals = Journal(
                            document.data["title"].toString(),
                            document.data.get("thoughts").toString(),
                            document.data.get("imageUri").toString(),
                            document.data.get("userId").toString(),
                            document.data.get("timestamp") as com.google.firebase.Timestamp,
                            document.data.get("userName").toString(),

                        )
                        journalList.add(journals)
                    }

                    //Recyclerview

                    binding.recyclerView.setAdapter(adapter)
                    adapter.notifyDataSetChanged()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this,
                    "Opps!  Something went wrong!",
                    Toast.LENGTH_SHORT
                ).show()
            }



    }
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity() // Finish the current activity and all parent activities
    }
}


