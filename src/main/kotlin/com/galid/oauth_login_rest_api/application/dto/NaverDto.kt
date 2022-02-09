package com.galid.oauth_login_rest_api.application.dto

import com.galid.oauth_login_rest_api.constants.Constants.NAVER_CLIENT_ID
import com.galid.oauth_login_rest_api.constants.Constants.NAVER_CLIENT_SECRET

class NaverDto {
    data class GetAccessTokenRequest(
        val grant_type: String = "authorization_code",
        val client_id: String = NAVER_CLIENT_ID,
        val client_secret: String = NAVER_CLIENT_SECRET,
        val code: String,
        val state: String? = null
    ) {
        override fun toString(): String {
            return "grant_type=${grant_type}" +
                    "&client_id=${client_id}" +
                    "&client_secret=${client_secret}" +
                    "&code=${code}" +
                    "&state=${state}"
        }
    }

    data class GetAccessTokenResponse(
        override val access_token: String?,
        override val refresh_token: String?,
        override val token_type: String?,
        override val expires_in: String?,
        override val error: String?,
        val error_description: String?
    ): com.galid.oauth_login_rest_api.application.dto.GetAccessTokenResponse(
        access_token = access_token,
        refresh_token = refresh_token,
        token_type = token_type,
        expires_in = expires_in,
        error = error
    )

    data class GetUserAccountResponse(
        val resultcode: String,
        val message: String,
        val response: Map<String, String>
    )
}