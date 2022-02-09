package com.galid.oauth_login_rest_api.application.dto

open class GetAccessTokenResponse(
    open val access_token: String?,
    open val refresh_token: String?,
    open val expires_in: String?,
    open val token_type: String?,
    open val error: String? = null
)