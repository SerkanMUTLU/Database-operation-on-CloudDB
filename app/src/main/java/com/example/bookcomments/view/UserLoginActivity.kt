package com.example.bookcomments.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.bookcomments.R
import com.example.bookcomments.dao.CloudDBZoneWrapper
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.agconnect.cloud.database.AGConnectCloudDB
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import kotlinx.android.synthetic.main.activity_user_login_page.*

class UserLoginActivity : AppCompatActivity() {
    val HUAWEIID_SIGNIN=123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_login_page)

        //sign out operation
        AGConnectAuth.getInstance().signOut()
        Log.w("Logout","Sign out begining of the App ")

        //declare without login button
        btnContinueWithoutLogin.setOnClickListener {
            continueWithoutLogin()
        }

        //declare User Login button
        btnUserLoginCheck.setOnClickListener {
            //call Login method
            requestLogin()
        }
    }

    private fun requestLogin(){
        //TODO Login Operation
        //get AuthParam from Auth Service
        val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
            .setAccessToken().createParams()
        val service= HuaweiIdAuthManager.getService(this, authParams)
        //start Activity for result to login
        startActivityForResult(service.signInIntent, HUAWEIID_SIGNIN)
    }

    //without Login, can't use these CloudDB operations: Upsert, Delete
    //can use these operations: Read, Query
    private fun continueWithoutLogin(){
        CloudDBZoneWrapper.initCloudDBZone()
        //changes activity from UserLoginActivity to MainActivity
        val withoutLoginIntent = Intent(applicationContext,MainActivity::class.java)
        Log.w("Login","User continue without login")
        startActivity(withoutLoginIntent)
    }

    //Method helps us to login
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == HUAWEIID_SIGNIN) {
            val authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            if (authHuaweiIdTask.isSuccessful) {
                CloudDBZoneWrapper.initCloudDBZone()
                /*
                    If User logged in our system before using this app,
                    App checks this state and then User will be redirected other page automatically
                    You have to login with HuaweiID to use the app
                 */

                val huaweiAccount = authHuaweiIdTask.result
                // we need access token to create credential.
                val accessToken = huaweiAccount.accessToken
                val credential = HwIdAuthProvider.credentialWithToken(accessToken)
                //
                AGConnectAuth.getInstance().signIn(credential)
                    .addOnSuccessListener { signInResult -> // onSuccess
                       //After sign in operation, user will be redirected MainPage of App.
                        goToMainActivity()
                    }.addOnFailureListener {
                    }
            }

            else {
                Log.w("Login Failed","Authentication Failed")
            }
        }
    }

    //Method is created to change Activity
    private fun goToMainActivity(){

        val userIntent = Intent(applicationContext,MainActivity::class.java)
        startActivity(userIntent)
    }
}