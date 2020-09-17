package com.example.bookcomments.model

data class Result<T> (val status: Status,val data:T?=null,val message:String?=null)