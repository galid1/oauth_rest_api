package com.galid.oauth_login_rest_api.application.dto

import com.galid.oauth_login_rest_api.constants.Constants.KAKAO_CLIENT_ID
import com.galid.oauth_login_rest_api.constants.Constants.KAKAO_REDIRECT_URI

class KakaoDto {
    data class GetAccessTokenRequest(
        val grant_type: String = "authorization_code",
        val client_id: String = KAKAO_CLIENT_ID,
        val redirect_uri: String = KAKAO_REDIRECT_URI,
        val code: String,
        val state: String? = null
    ) {
        override fun toString(): String {
            return "grant_type=${grant_type}" +
                    "&client_id=${client_id}" +
                    "&redirect_uri=${redirect_uri}" +
                    "&code=${code}" +
                    "&state=${state}"
        }
    }

    data class GetAccessTokenResponse(
        override val access_token: String?,
        override val refresh_token: String? = null,
        override val token_type: String? = null,
        override val expires_in: String? = null,
        val refresh_token_expires_in: Int? = null,
        val scope: String? = null
    ): com.galid.oauth_login_rest_api.application.dto.GetAccessTokenResponse(
        access_token = access_token,
        refresh_token = refresh_token,
        token_type = token_type,
        expires_in = expires_in
    )

    data class GetUserAccountResponse(
        val id: String,
        val connected_at: String,
        val kakao_account: Map<String, String>
    )
}