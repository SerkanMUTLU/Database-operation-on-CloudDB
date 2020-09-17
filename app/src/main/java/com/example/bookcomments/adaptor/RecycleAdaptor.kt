package com.example.bookcomments.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookcomments.R
import com.example.bookcomments.model.Comment
import kotlinx.android.synthetic.main.comment_recycle_row.view.*

class RecycleAdaptor(private val commentList:ArrayList<Comment>,var clickListener: OnCommentItemClickListener)
    : RecyclerView.Adapter<RecycleAdaptor.CommentViewHolder>(){

    class CommentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val txtVwComment : TextView = itemView.txtVwRecyclComment
        val txtVwCommentDate : TextView = itemView.txtVwRcycleDate
        val txtVwPersonName : TextView = itemView.txtRecyRowPersonName
        val txtVwBookName : TextView = itemView.txtVwBookName


        fun initialize(item:Comment,action:OnCommentItemClickListener){
            itemView.setOnClickListener {
                action.onItemClick(item,adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.comment_recycle_row,parent,false)

        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.txtVwBookName.text = "Book name :"+commentList[position].BookName
        holder.txtVwComment.text = "Comment : "+commentList[position].Comments
        holder.txtVwPersonName.text = "Person : "+commentList[position].Person

        //Comment date is handled at here
        holder.txtVwCommentDate.text ="Comment Date : "+ commentList[position].CommentDate

        holder.initialize(commentList[position],clickListener)

    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    fun updateCommentList(commentsList : ArrayList<Comment>){
        commentList.clear()
        commentList.addAll(commentsList)
        notifyDataSetChanged()
    }

}

    interface OnCommentItemClickListener{
        fun onItemClick(item:Comment,position: Int)
    }
