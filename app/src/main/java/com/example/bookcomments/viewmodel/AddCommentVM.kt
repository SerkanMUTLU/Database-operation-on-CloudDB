package com.example.bookcomments.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bookcomments.dao.CloudDBZoneWrapper
import com.example.bookcomments.model.Comment
import com.example.bookcomments.model.QueryResult
import com.example.bookcomments.model.Status
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class AddCommentVM(application: Application):AndroidViewModel(application) {

    fun addNewRecordToCloudDB(comment: Comment):String{
        //It is used for getting time when Users insert new comment
        var date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))

        //CloudDB doesn't include Auto generated primary key so we create a random id
        var commentID = (100000..999999).shuffled().first()

        comment.CommentDate=date
        comment.Id=commentID.toString()

        // run upsert method and get the result
        var result = CloudDBZoneWrapper.upsertDataInCloudDB(comment)

        return if(result.status == Status.Error){
                QueryResult.Failed.result
        }else{
            QueryResult.Success.result
        }

    }

}