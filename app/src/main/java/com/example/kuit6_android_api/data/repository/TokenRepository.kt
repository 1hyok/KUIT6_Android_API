package com.example.kuit6_android_api.data.repository

import android.content.Context
import com.example.kuit6_android_api.data.model.response.BaseResponse

interface TokenRepository {
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun saveAutoLogin(isAutoLogin: Boolean)
    suspend fun getAutoLogin(): Boolean
    // 토큰 삭제하는 함수
    suspend fun deleteToken()
    // 토큰 검증 함수
    suspend fun validateToken(context: Context): Result<Boolean>
}