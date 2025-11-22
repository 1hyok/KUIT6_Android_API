package com.example.kuit6_android_api.data.repository

import javax.inject.Singleton

interface TokenApiRepository {
    suspend fun validateToken():Result<Boolean>
}