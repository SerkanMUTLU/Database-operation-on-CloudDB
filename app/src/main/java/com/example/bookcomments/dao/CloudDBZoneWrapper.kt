package com.example.bookcomments.dao


import android.util.Log
import com.example.bookcomments.model.Comment
import com.example.bookcomments.model.ObjectTypeInfoHelper
import com.example.bookcomments.model.Result
import com.example.bookcomments.model.Status
import com.huawei.agconnect.cloud.database.*

object CloudDBZoneWrapper {

        //This class can be used for Database operations CRUD .All CRUD function must be at here
        private lateinit var cloudDB: AGConnectCloudDB
        private  lateinit var cloudDbZone:CloudDBZone
        private  lateinit var cloudDBZoneConfig: CloudDBZoneConfig

        /*
            App needs to initialize before using. All Developer must follow sequence of Cloud DB
             (1)Before these operations AGConnectCloudDB.initialize(context) method must be called
             (2)init AGConnectCloudDB
             (3)create object type
             (4)open cloudDB zone
             (5)CRUD if all is ready!
        */
        //TODO getInstance of AGConnectCloudDB
        fun initCloudDBZone(){
            cloudDB = AGConnectCloudDB.getInstance()
            createObjectType()
            openCloudDBZone()
        }

         //Call AGConnectCloudDB.createObjectType to init
        fun createObjectType(){
            try{
                if(cloudDB == null){
                    Log.w("Result","CloudDB Yok")
                    return
                }
                cloudDB.createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo())

            }catch (e:Exception){
                Log.w("Create Object Type",e)
            }
        }

        /*
             Call AGConnectCloudDB.openCloudDBZone to open a cloudDBZone.
             We set it with cloud cache mode, and data can be stored in local storage
         */

        fun openCloudDBZone(){
            /*
                declared CloudDBZone and configure it.
                First Parameter of CloudDBZoneConfig is used to specify CloudDBZone name that was declared on App Gallery
            */
            //TODO specify CloudDBZone Name and Its properties


            cloudDBZoneConfig = CloudDBZoneConfig("BookComment",
                  CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                  CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC)
            cloudDBZoneConfig.persistenceEnabled=true

            try{
                cloudDbZone = cloudDB.openCloudDBZone(cloudDBZoneConfig,true)
            }catch (e:Exception){
                Log.w("Open CloudDB Zone ",e)
            }
        }

        //Function returns all comments from CloudDB.
        fun getAllDataFromCloudDB():ArrayList<Comment>{

            var allComments = arrayListOf<Comment>()

            //TODO create a query to select data
            val cloudDBZoneQueryTask =cloudDbZone.executeQuery(CloudDBZoneQuery
                .where(Comment::class.java),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)

            //If you want to get data as async, you can add listener instead of cloudDBZoneQueryTask.result
            cloudDBZoneQueryTask.await()

            if(cloudDBZoneQueryTask.result == null){
                Log.w("CloudDBQuery",cloudDBZoneQueryTask.exception)
                return allComments
            }else{
                // we can get result from cloudDB using cloudDBZoneQueryTask.result.snapshotObjects
                val myResult = cloudDBZoneQueryTask.result.snapshotObjects

                //Get all data from CloudDB to our Arraylist Variable
                if(myResult!= null){
                    while (myResult.hasNext()){
                        var item = myResult.next()
                        allComments.add(item)
                    }
                }
                return  allComments
            }
        }

        //   Call AGConnectCloudDB.upsertDataInCloudDB
        fun upsertDataInCloudDB(newComment:Comment):Result<Any?>{

            //TODO choose execute type like executeUpsert
            var upsertTask : CloudDBZoneTask<Int> = cloudDbZone.executeUpsert(newComment)

            upsertTask.await()

            if(upsertTask.exception != null){
                Log.e("UpsertOperation",upsertTask.exception.toString())
                return Result(Status.Error)
            }else{
                return Result(Status.Success)
            }
        }

        //Call AGConnectCloudDB.deleteCloudDBZone
        fun deleteDataFromCloudDB(selectedItem:Comment):Result<Any?>{

            //TODO choose execute type like executeDelete
            val cloudDBDeleteTask = cloudDbZone.executeDelete(selectedItem)

            cloudDBDeleteTask.await()

            if(cloudDBDeleteTask.exception != null){
                Log.e("CloudDBDelete",cloudDBDeleteTask.exception.toString())
                return Result(Status.Error)
            }else{
                return Result(Status.Success)
            }
        }

        //Queries all Comments by Book Name from cloud side with CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        fun searchCommentByBookName(bookName:String):ArrayList<Comment>{
            var allComments : ArrayList<Comment> = arrayListOf()

            //Query : If you want to search book item inside the Data set, you can use it
            val cloudDBZoneQueryTask =cloudDbZone.executeQuery(CloudDBZoneQuery
                .where(Comment::class.java).contains("BookName",bookName),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY)

            cloudDBZoneQueryTask.await()

            if(cloudDBZoneQueryTask.result ==null){
                Log.e("Error",cloudDBZoneQueryTask.exception.toString())
                return allComments
            }else{
                //take result of query
                val bookResult = cloudDBZoneQueryTask.result.snapshotObjects

                while (bookResult.hasNext()){
                    var item = bookResult.next()
                    allComments.add(item)
                }
                return allComments
            }
        }

        //TODO Close Cloud db zone
        //Call AGConnectCloudDB.closeCloudDBZone
        fun closeCloudDBZone(){
            try {
                cloudDB.closeCloudDBZone(cloudDbZone)
                Log.w("CloudDB zone close","Cloud was closed")
            }catch (e:Exception){
                Log.w("CloudDBZone",e)
            }
        }
    }
