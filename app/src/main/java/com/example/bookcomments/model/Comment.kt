package com.example.bookcomments.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.PrimaryKey
import java.io.Serializable
/*
    Our model class, each column must be same with CloudDB column name.
*/
data class Comment(
    var BookName:String,
    var ISBN:String,
    var Comments:String,
    var Author:String,
    var Person: String,
    var PrintingHouse:String
) : CloudDBZoneObject(),Serializable {

    @PrimaryKey //Describes this column as a Primary key
    lateinit var Id:String
    lateinit var CommentDate: String

}