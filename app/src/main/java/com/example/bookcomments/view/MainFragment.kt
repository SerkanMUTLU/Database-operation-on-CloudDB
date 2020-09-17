package com.example.bookcomments.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookcomments.R
import com.example.bookcomments.adaptor.OnCommentItemClickListener
import com.example.bookcomments.adaptor.RecycleAdaptor
import com.example.bookcomments.dao.CloudDBZoneWrapper
import com.example.bookcomments.model.Comment
import com.example.bookcomments.viewmodel.MainFragmentVM
import com.huawei.agconnect.auth.AGConnectAuth
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() , OnCommentItemClickListener {

    private lateinit var lytView : View
    private lateinit var mainFragmentVM  : MainFragmentVM
    private lateinit var commentAdaptor :RecycleAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        lytView = inflater.inflate(R.layout.fragment_main, container, false)
        return lytView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel is declared under line !
        mainFragmentVM = ViewModelProvider(this).get(MainFragmentVM::class.java)

        setRecyclerView()//Call recycler View method that includes related settings

        setSwipeRefreshLayout()//Call swipe refresh layout

        //set search button click listener
        btnSearchBookName.setOnClickListener {
            searchBookOnCloudDB()
        }


        //Insert and Update buttons declared at here
        btnAddComment.setOnClickListener {
            setInsertButtonClick(it)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

            //Add an observer to commentListFromCloudDB
            mainFragmentVM.commentList.observe(viewLifecycleOwner, Observer {
                it?.let {
                    commentAdaptor.updateCommentList(it as ArrayList<Comment>)
                }
            })
    }

    //Method includes Recycler View settings
    private fun setRecyclerView(){
        //Set Recycler ViewAdaptor with cloudDB's data
        commentAdaptor = RecycleAdaptor(arrayListOf(),this)
        //Set Recycler View's layout manager
        bookCommentRecycleView.layoutManager = LinearLayoutManager(context)
        //Set RecyclerView's adaptor
        bookCommentRecycleView.adapter=commentAdaptor
    }

    //Set RefreshLayout feature that is RefreshListener
    private fun setSwipeRefreshLayout(){
        swipeRefreshLyt.setOnRefreshListener {
            //After Refresh layout, app will be taken data from CloudDB
            mainFragmentVM.getAllCommentFromCloudDB()

            //set swipe Refresh layout's loading Symbol
            swipeRefreshLyt.isRefreshing = false

            //After refresh layout, delete and update button won't be shown to Users
            showButtons(false)
        }
    }

    //add listener for click RecyclerView action
    override fun onItemClick(item: Comment, position: Int) {

        if(item != null){
            //When user click recyclerView Update and Delete buttons will be displayed
            showButtons(true)

            btnUpdateComment.setOnClickListener{
                setUpdateButtonClick(item,it)
            }

            btnDeleteCommentsSelectr.setOnClickListener {
                deleteCommentFromCloudDB(item)
            }
        }
    }

    /*
        UpdateCommentFragment will be opened When the user clicks recycler view row and then clicks update button that is at the top of the screen
    */
    private fun setUpdateButtonClick(selectedComment:Comment,view:View){
        val actionToUpdateFragment = MainFragmentDirections.actionMainFragmentToUpdateCommentFragment(selectedComment)
        Navigation.findNavController(view).navigate(actionToUpdateFragment)
    }

    /*
         When the user wants to insert new comment, click on insert comment button
        and then related fragment will be displayed
    */
    private fun setInsertButtonClick(view: View){
        val actionToChangeScreen = MainFragmentDirections.actionMainFragmentToAddCommentFragment()
        Navigation.findNavController(view).navigate(actionToChangeScreen)
    }

    /*
        Delete button's click listener
        If User clicks on delete button then selects an item from Recycler View,
        Selected Item will be deleted from CloudDB
     */
   private fun deleteCommentFromCloudDB(comment: Comment){
             //Delete methods calling
            var operationResult = mainFragmentVM.deleteCommentFromDB(comment)

            Toast.makeText(context,operationResult,Toast.LENGTH_SHORT).show()

            showButtons(false)

    }

    //When user clicks on recyclerView, update and delete button will be displayed
    private fun showButtons(state:Boolean){
        if(!state){
            btnDeleteCommentsSelectr.visibility=View.INVISIBLE
            btnUpdateComment.visibility=View.INVISIBLE
        }else{
            btnDeleteCommentsSelectr.visibility=View.VISIBLE
            btnUpdateComment.visibility=View.VISIBLE
        }
    }

    //Method was created to search book on CloudDB
    private fun searchBookOnCloudDB(){
            //Get data from editTextBox
           var bookName = edtTxtSearchBook.text.trim().toString()

           if(bookName.isEmpty()){
               Toast.makeText(context,"You should book name then click Search button",Toast.LENGTH_LONG).show()
           }else{
               mainFragmentVM.searchedBookByName(bookName)
               edtTxtSearchBook.text.clear()
           }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(context,"Log out",Toast.LENGTH_SHORT).show()
        //Current user will sign out after closing fragment or going back to login page.
        CloudDBZoneWrapper.closeCloudDBZone()

        AGConnectAuth.getInstance().signOut()
        Log.w("Sign out ","Log out ")
    }
}