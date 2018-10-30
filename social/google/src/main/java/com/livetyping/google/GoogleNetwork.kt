package com.livetyping.google

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.livetyping.logincore.SocialLoginError
import com.livetyping.logincore.SocialNetwork

class GoogleNetwork(serverClientId: String) : SocialNetwork {
    companion object {
        private const val GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE = 231
    }

    private val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .build()

    override fun login(activity: Activity) {
        val signInClient = GoogleSignIn.getClient(activity, signInOptions)
        activity.startActivityForResult(signInClient.signInIntent, GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?,
                                  successBlock: (token: String) -> Unit,
                                  errorBlock: ((error: SocialLoginError) -> Unit)?) {
        if (requestCode == GOOGLE_SIGN_IN_ACTIVITY_REQUEST_CODE){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                successBlock(account!!.idToken!!)
            } catch (e: ApiException) {
                errorBlock?.invoke(GoogleLoginError(e))
            }
        }
    }
}