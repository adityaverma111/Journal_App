package com.example.journalapp

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class JournalRecyclerAdapter( private val itmlst: List<Journal>)

    : RecyclerView.Adapter<JournalRecyclerAdapter.MyViewHolder>()
{
   public  class MyViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView){


        val username: TextView = itemView.findViewById(R.id.journal_row_username)
        val imageview : ImageView = itemView.findViewById(R.id.journal_image_list)
        val journaltitle : TextView = itemView.findViewById(R.id.journal_title_list)
        val journalThoughts : TextView = itemView.findViewById(R.id.journal_thought_list)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.journal_row,parent,false)
        return MyViewHolder(itemView,)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = itmlst[position]
      // Glide.with(context).load(Journal_list[position].imageUri).into(holder.imageview)
        holder.username.text = currentitem.userName.toString()
        holder.journaltitle.text = currentitem.title.toString()
        holder.journalThoughts.text = currentitem.thoughts.toString()
       /* val bytes = android.util.Base64.decode(currentitem.imageUri,
            Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.size)
        holder.imageview.setImageBitmap(bitmap)*/



    }
    override fun getItemCount(): Int {
        return  itmlst.size
    }


}
   // lateinit var  binding: JournalRowBinding









