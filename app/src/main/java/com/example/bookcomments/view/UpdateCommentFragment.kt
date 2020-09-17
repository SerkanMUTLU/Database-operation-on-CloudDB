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
import com.example.bookcomments.viewmodel.UpdateCommentVM
import kotlinx.android.synthetic.main.fragment_update_comment.*

class UpdateCommentFragment : Fragment() {

    private lateinit var lytView : View
    private lateinit var selectedComment:Comment
    private lateinit var updateCommentMV:UpdateCommentVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Get selected data from Main Activity
        arguments?.let {
            var args = UpdateCommentFragmentArgs.fromBundle(it)
            selectedComment = args.selectedComment
        }

        //create instance from UpdateCommentMV
        updateCommentMV = ViewModelProvider(this).get(UpdateCommentVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        lytView = inflater.inflate(R.layout.fragment_update_comment, container, false)
        return lytView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDataToComponent()
        setUpdateButtonListener()//set btnCommentUpdate click Listener
    }

    //set components with selected Item's data
    private fun setDataToComponent(){
        edtTxtAuthor.setText(selectedComment.Author)
        edtTxtBookName.setText(selectedComment.BookName)
        edtTxtComment.setText(selectedComment.Comments)
        edtTxtISBN.setText(selectedComment.ISBN)
        edtTxtPerson.setText(selectedComment.Person)
        edtTxtPrintingHouse.setText(selectedComment.PrintingHouse)
    }

    //Get new Data from components
    private fun getAllDataFromComponent(){

        var isbn = edtTxtISBN.text.toString()
        var bookName = edtTxtBookName.text.toString()
        var comment = edtTxtComment.text.toString()
        var authorFullName = edtTxtAuthor.text.toString()
        var personFullName =edtTxtPerson.text.toString()
        var printingHouse = edtTxtPrintingHouse.text.toString()

        selectedComment.ISBN=isbn
        selectedComment.BookName=bookName
        selectedComment.Comments=comment
        selectedComment.Author=authorFullName
        selectedComment.Person=personFullName
        selectedComment.PrintingHouse=printingHouse

        val operationResult = updateCommentMV.updateComment(selectedComment)

        Toast.makeText(context,operationResult,Toast.LENGTH_SHORT).show()

    }

    //after click related button , Selected Comment will be changed
    private fun setUpdateButtonListener(){
        btnCommentUpdate.setOnClickListener{
            getAllDataFromComponent()
        }
    }

}