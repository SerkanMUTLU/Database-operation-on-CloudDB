package com.example.bookcomments.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.bookcomments.R
import com.example.bookcomments.model.Comment
import com.example.bookcomments.viewmodel.AddCommentVM
import kotlinx.android.synthetic.main.fragment_add_comment.*
import kotlinx.android.synthetic.main.fragment_main.btnAddComment



class AddCommentFragment : Fragment() {

    private lateinit var lytView : View

    private lateinit var addCommentVM : AddCommentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //get instance of add comment view model
        addCommentVM = ViewModelProvider(this).get(AddCommentVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        lytView = inflater.inflate(R.layout.fragment_add_comment, container, false)
        return lytView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAddComment.setOnClickListener {
            addNewComment()
        }
    }

    //call insert method
    private fun addNewComment(){
        var result = addCommentVM.addNewRecordToCloudDB(collectInformationFromComponent())

        Toast.makeText(context,result,Toast.LENGTH_SHORT).show()
    }

    //get new information from components
    private fun collectInformationFromComponent(): Comment {

        var isbn = edtTxtAddISBN.text.toString()
        var bookName = edtTxtAddBookName.text.toString()
        var comment = edtTxtAddComment.text.toString()
        var authorFullName = edtTxtAddAuthor.text.toString()
        var personFullName =edtTxtAddPerson.text.toString()
        var printingHouse = edtTxtAddPrintingHouse.text.toString()

        return Comment(bookName,isbn,comment,authorFullName,personFullName,printingHouse)
    }

}