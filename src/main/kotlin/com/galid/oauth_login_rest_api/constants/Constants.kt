package com.galid.oauth_login_rest_api.constants

object Constants {
    // KAKAO
    const val KAKAO_AUTH_HOST_URI = "https://kauth.kakao.com"
    const val KAKAO_REDIRECT_URI = ""
    const val KAKAO_GET_ACCESS_TOKEN_URI = "$KAKAO_AUTH_HOST_URI/oauth/token"
    const val KAKAO_GET_EMAIL_API_HOST_URI = "https://kapi.kakao.com/v2/user/me"

    const val KAKAO_CLIENT_ID = ""

    const val KAKAO_LOGIN_URL = "${KAKAO_AUTH_HOST_URI}/oauth/authorize?" +
            "client_id=${KAKAO_CLIENT_ID}" +
            "&redirect_uri=${KAKAO_REDIRECT_URI}" +
            "&response_type=code" +
            "&scope=account_email"

    // NAVER
    const val NAVER_AUTH_HOST_URI = "https://nid.naver.com"
    const val NAVER_REDIRECT_URI = ""
    const val NAVER_GET_ACCESS_TOKEN_URI = "$NAVER_AUTH_HOST_URI/oauth2.0/token"
    const val NAVER_GET_EMAIL_API_HOST_URI = "https://openapi.naver.com/v1/nid/me"

    const val NAVER_CLIENT_ID = ""
    const val NAVER_CLIENT_SECRET = ""

    const val NAVER_LOGIN_URL = "${NAVER_AUTH_HOST_URI}/oauth2.0/authorize?" +
            "response_type=code" +
            "&client_id=${NAVER_CLIENT_ID}" +
            "&redirect_uri=${NAVER_REDIRECT_URI}"

    // GOOGLE
    const val GOOGLE_AUTH_HOST_URI = "https://accounts.google.com"
    const val GOOGLE_REDIRECT_URI = ""
    const val GOOGLE_GET_ACCESS_TOKEN_URI = "https://oauth2.googleapis.com/token"
    const val GOOGLE_GET_EMAIL_API_HOST_URI = "https://www.googleapis.com/oauth2/v3/userinfo"

    const val GOOGLE_CLIENT_ID = ""
    const val GOOGLE_CLIENT_SECRET = ""

    const val GOOGLE_LOGIN_URL = "${GOOGLE_AUTH_HOST_URI}/o/oauth2/v2/auth?" +
            "client_id=${GOOGLE_CLIENT_ID}" +
            "&redirect_uri=${GOOGLE_REDIRECT_URI}" +
            "&response_type=code" +
            "&scope=https://www.googleapis.com/auth/userinfo.email" +
            "&access_type=offline"

    enum class SupportSocials(
        val socialName: String
    ) {
        KAKAO("KAKAO"), NAVER("NAVER"), GOOGLE("GOOGLE")
    }
}