package com.galid.oauth_login_rest_api.controller

import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.http.HttpHeaders.*
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.MediaType.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.BodyInserter
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@RestController
@RequestMapping("/oauth/callback")
class OauthCallbackController(
    private val webClient: WebClient,
    private val objectMapper: ObjectMapper
) {
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
            SupportSocials.valueOf(socialName.uppercase())
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
        val result = webClient.get()
            .uri(getApiHostUri(social))
            .header(AUTHORIZATION, "Bearer $accessToken")
            .retrieve()
            .bodyToMono(getResultType(social))
            .block()

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

    private fun getResultType(social: SupportSocials) = when (social) {
        KAKAO -> KakaoDto.GetUserAccountResponse::class.java
        NAVER -> NaverDto.GetUserAccountResponse::class.java
        GOOGLE -> GoogleDto.GetUserAccountResponse::class.java
    }

    private fun getApiHostUri(social: SupportSocials) = when (social) {
        KAKAO -> KAKAO_GET_EMAIL_API_HOST_URI
        NAVER -> NAVER_GET_EMAIL_API_HOST_URI
        GOOGLE -> GOOGLE_GET_EMAIL_API_HOST_URI
    }

    private fun getAccessToken(
        social: SupportSocials,
        code: String,
        state: String?
    ): String {
        val requestBody = makeGetAccessTokenHttpRequestBody(
            social = social,
            code = code,
            state = state
        )

        var webClient = webClient.post()
            .uri(getAccessTokenUri(social))
            .body(BodyInserters.fromValue(requestBody))

        if (social == KAKAO || social == NAVER) {
            webClient = webClient
                .header(CONTENT_TYPE, APPLICATION_FORM_URLENCODED_VALUE)
        }

        return webClient
            .retrieve()
            .bodyToMono(GetAccessTokenResponse::class.java)
            .block()
            ?.access_token
            ?: throw RuntimeException("Resource Server로 AccessToken 요청에 실패했습니다.")
    }

    private fun getAccessTokenUri(social: SupportSocials) =
        when (social) {
            KAKAO -> KAKAO_GET_ACCESS_TOKEN_URI
            NAVER -> NAVER_GET_ACCESS_TOKEN_URI
            GOOGLE -> GOOGLE_GET_ACCESS_TOKEN_URI
        }

    internal fun makeGetAccessTokenHttpRequestBody(
        social: SupportSocials,
        code: String,
        state: String?
    ): String {
        return when (social) {
            KAKAO -> {
                KakaoDto.GetAccessTokenRequest(code = code, state = state).toString()
            }
            NAVER -> {
                NaverDto.GetAccessTokenRequest(code = code, state = state).toString()
            }
            GOOGLE -> {
                objectMapper.writeValueAsString(
                    GoogleDto.GetAccessTokenRequest(code = code, state = state)
                )
            }
        }
    }
}