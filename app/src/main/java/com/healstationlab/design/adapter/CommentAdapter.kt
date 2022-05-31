package com.healstationlab.design.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.healstationlab.design.R
import com.healstationlab.design.model.Comment

class CommentAdapter(private val commentList : ArrayList<Comment>) : RecyclerView.Adapter<CommentAdapter.Holder>() {

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(commentList[position])
    }

    inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val profileimg = itemView.findViewById<ImageView>(R.id.profile_img)!!
        val nickname = itemView.findViewById<TextView>(R.id.nick_name)!!
        val content = itemView.findViewById<TextView>(R.id.reply_content)!!
        val date = itemView.findViewById<TextView>(R.id.date)!!

        fun bind(comment : Comment){
            if(comment.imageUrl == "" || comment.imageUrl == "null"){
                Glide.with(itemView).load(R.drawable.pro2).into(profileimg)
            } else {
                Glide.with(itemView).load(comment.imageUrl).into(profileimg)
            }
            nickname.text = comment.nick_name
            content.text = comment.content
            date.text = comment.date
        }
    }
}