package com.galid.oauth_login_rest_api.controller

import com.galid.oauth_login_rest_api.constants.Constants.GOOGLE_LOGIN_URL
import com.galid.oauth_login_rest_api.constants.Constants.KAKAO_LOGIN_URL
import com.galid.oauth_login_rest_api.constants.Constants.NAVER_LOGIN_URL
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/oauth")
class OauthController {
    @GetMapping("/kakao")
    fun kakao(): ResponseEntity<Void> {
        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .header(HttpHeaders.LOCATION, KAKAO_LOGIN_URL)
            .build()
    }

    @GetMapping("/naver")
    fun naver(): ResponseEntity<Void> {
        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .header(HttpHeaders.LOCATION, NAVER_LOGIN_URL)
            .build()
    }

    @GetMapping("/google")
    fun google(): ResponseEntity<Void> {
        return ResponseEntity
            .status(HttpStatus.MOVED_PERMANENTLY)
            .header(HttpHeaders.LOCATION, GOOGLE_LOGIN_URL)
            .build()
    }
}