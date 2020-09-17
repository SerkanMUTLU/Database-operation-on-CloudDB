package com.example.bookcomments.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookcomments.dao.CloudDBZoneWrapper
import com.example.bookcomments.model.Comment
import com.example.bookcomments.model.QueryResult
import com.example.bookcomments.model.Status

class MainFragmentVM(application: Application) : AndroidViewModel(application) {

    // allows only read data from this list, can't change any value from outside of ViewModel
    val commentList: LiveData<List<Comment>>
        get()=_commentList

    private val _commentList = MutableLiveData<List<Comment>>()

    init{
        //Call related method and handle all Comment data come from CloudDB
        getAllCommentFromCloudDB()
    }


     //Method is used to get all comments from CloudDB .
     fun getAllCommentFromCloudDB(){
         var commentArrayList =CloudDBZoneWrapper.getAllDataFromCloudDB()
         if(commentArrayList.size == 0){
             Toast.makeText(getApplication(),"You can use swipe refresh layout ",Toast.LENGTH_LONG).show()
         }else{
             _commentList.value = commentArrayList
         }
     }

    /*
       search book in CloudDB, need to call this method that has a parameter that stores book name
     */
    fun searchedBookByName(bookName:String){
            _commentList.value= CloudDBZoneWrapper.searchCommentByBookName(bookName)
    }

    // delete comment inside CloudDB, call deleteCommentFromDB() method
    fun deleteCommentFromDB(selectedComment:Comment):String{
        var deleteResult = CloudDBZoneWrapper.deleteDataFromCloudDB(selectedComment)

        return if (deleteResult.status == Status.Success){
            QueryResult.Success.result
        }else{
            QueryResult.Failed.result
        }
    }

}

