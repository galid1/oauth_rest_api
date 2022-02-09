package com.galid.oauth_login_rest_api.controller

import com.galid.oauth_login_rest_api.application.dto.GetAccessTokenResponse
import com.galid.oauth_login_rest_api.application.dto.GoogleDto
import com.galid.oauth_login_rest_api.application.dto.KakaoDto
import com.galid.oauth_login_rest_api.application.dto.NaverDto
import com.galid.oauth_login_rest_api.constants.Constants.GOOGLE_GET_ACCESS_TOKEN_URI
import com.galid.oauth_login_rest_api.constants.Constants.GOOGLE_GET_EMAIL_API_HOST_URI
import com.galid.oauth_login_rest_api.constants.Constants.KAKAO_GET_ACCESS_TOKEN_URI
import com.galid.oauth_login_rest_api.constants.Constants.KAKAO_GET_EMAIL_API_HOST_URI
import com.galid.oauth_login_rest_api.constants.Constants.NAVER_GET_ACCESS_TOKEN_URI
import com.galid.oauth_login_rest_api.constants.Constants.NAVER_GET_EMAIL_API_HOST_URI
import com.galid.oauth_login_rest_api.constants.Constants.SupportSocials
import com.galid.oauth_login_rest_api.constants.Constants.SupportSocials.*
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

@RestController
@RequestMapping("/oauth/callback")
class OauthCallbackController {
    @GetMapping("/{social_name}")
    fun socialLoginCallback(
        @PathVariable("social_name") socialName: String,
        @RequestParam("code") code: String?,
        @RequestParam("state") state: String?,
        @RequestParam("error") error: String?,
        @RequestParam("error_description") errorDescription: String?
    ) {
        if (code == null) {
            throw RuntimeException("code가 존재하지 않습니다.")
        }

        val social = try {
            valueOf(socialName.uppercase())
        } catch (e: IllegalArgumentException) {
            throw RuntimeException("지원하지 않는 Social입니다.")
        }

        val email = getUserEmailByAccessTokenFromResourceServer(
            social = social,
            accessToken = getAccessToken(
                social = social,
                code = code,
                state = state
            )
        )

        println("email : ${email}")
    }

    internal fun getUserEmailByAccessTokenFromResourceServer(
        social: SupportSocials,
        accessToken: String
    ): String {
        val headers = LinkedMultiValueMap<String, String>()
        headers[HttpHeaders.AUTHORIZATION] = "Bearer ${accessToken}"

        val httpClient = RestTemplate()

        val getUserEmailApiHostUri = when(social) {
            KAKAO -> KAKAO_GET_EMAIL_API_HOST_URI
            NAVER -> NAVER_GET_EMAIL_API_HOST_URI
            GOOGLE -> GOOGLE_GET_EMAIL_API_HOST_URI
        }

        val resultType = when(social) {
            KAKAO -> KakaoDto.GetUserAccountResponse::class.java
            NAVER -> NaverDto.GetUserAccountResponse::class.java
            GOOGLE -> GoogleDto.GetUserAccountResponse::class.java
        }

        val result = httpClient.exchange(
            getUserEmailApiHostUri,
            HttpMethod.GET,
            HttpEntity<Void>(HttpHeaders(headers)),
            resultType,
            null
        ).body

        return when(social) {
            KAKAO -> {
                result as KakaoDto.GetUserAccountResponse
                result.kakao_account["email"]!!
            }
            NAVER -> {
                result as NaverDto.GetUserAccountResponse
                result.response["email"]!!
            }
            GOOGLE -> {
                result as GoogleDto.GetUserAccountResponse
                result.email
            }
        }
    }

    internal fun getAccessToken(
        social: SupportSocials,
        code: String,
        state: String?
    ): String {
        val getAccessTokenHttpRequest = makeGetAccessTokenHttpRequest(
            social = social,
            code = code,
            state = state
        )

        val httpClient = RestTemplate()

        val getAccessTokenResult = when (social) {
            KAKAO -> {
                httpClient.postForEntity(
                    KAKAO_GET_ACCESS_TOKEN_URI,
                    getAccessTokenHttpRequest,
                    GetAccessTokenResponse::class.java,
                )
            }
            NAVER -> {
                httpClient.postForEntity(
                    NAVER_GET_ACCESS_TOKEN_URI,
                    getAccessTokenHttpRequest,
                    GetAccessTokenResponse::class.java
                )
            }
            GOOGLE -> {
                httpClient.postForEntity(
                    GOOGLE_GET_ACCESS_TOKEN_URI,
                    getAccessTokenHttpRequest,
                    GetAccessTokenResponse::class.java
                )
            }
        }
            .body
            ?: throw RuntimeException("access Token을 가져오는 과정에서 exception이 발생했습니다.")

        return getAccessTokenResult
            .access_token
            ?: throw RuntimeException(getAccessTokenResult.error ?: "알 수 없는 오류로 access Token을 가져오지 못했습니다.")
    }

    internal fun makeGetAccessTokenHttpRequest(
        social: SupportSocials,
        code: String,
        state: String?
    ): HttpEntity<Any> {
        return when (social) {
            KAKAO -> {
                val headers = LinkedMultiValueMap<String, String>()
                headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_FORM_URLENCODED_VALUE

                val body = KakaoDto.GetAccessTokenRequest(code = code, state = state)
                HttpEntity(body.toString(), headers)
            }
            NAVER -> {
                val headers = LinkedMultiValueMap<String, String>()
                headers[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_FORM_URLENCODED_VALUE

                val body = NaverDto.GetAccessTokenRequest(code = code, state = state)
                HttpEntity(body.toString(), headers)
            }
            GOOGLE -> {
                val body = GoogleDto.GetAccessTokenRequest(code = code, state = state)
                HttpEntity(body)
            }
        }
    }
}