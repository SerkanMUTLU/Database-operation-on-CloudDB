package com.example.bookcomments.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.bookcomments.dao.CloudDBZoneWrapper
import com.example.bookcomments.model.Comment
import com.example.bookcomments.model.QueryResult
import com.example.bookcomments.model.Status

class UpdateCommentVM(application: Application):AndroidViewModel(application) {

    fun updateComment(selectedComment:Comment):String{

        var updateResult = CloudDBZoneWrapper.upsertDataInCloudDB(selectedComment)

        //Show executeUpsert result if It is successfully it will redirect success otherwise Failed
        return if(updateResult.status == Status.Error){
            QueryResult.Failed.result
        }else{
            QueryResult.Success.result
        }
    }

}