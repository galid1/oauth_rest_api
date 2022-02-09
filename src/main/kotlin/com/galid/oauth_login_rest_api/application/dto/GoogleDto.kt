package com.galid.oauth_login_rest_api.application.dto

import com.galid.oauth_login_rest_api.constants.Constants.GOOGLE_CLIENT_ID
import com.galid.oauth_login_rest_api.constants.Constants.GOOGLE_CLIENT_SECRET
import com.galid.oauth_login_rest_api.constants.Constants.GOOGLE_REDIRECT_URI

class GoogleDto {
    data class GetAccessTokenRequest(
        val grantType: String = "authorization_code",
        val clientId: String = GOOGLE_CLIENT_ID,
        val clientSecret: String = GOOGLE_CLIENT_SECRET,
        val redirectUri: String = GOOGLE_REDIRECT_URI,
        val code: String,
        val state: String? = null
    )

    data class GetAccessTokenResponse(
        override val access_token: String?,
        override val refresh_token: String?,
        override val token_type: String?,
        override val expires_in: String?,
        val scope: String?,
        val id_token: String,
    ): com.galid.oauth_login_rest_api.application.dto.GetAccessTokenResponse(
        access_token = access_token,
        refresh_token = refresh_token,
        token_type = token_type,
        expires_in = expires_in,
    )

    data class GetUserAccountResponse(
        val sub: String,
        val picture: String,
        val email: String,
        val email_verified: String,
        val hd: String
    )
}