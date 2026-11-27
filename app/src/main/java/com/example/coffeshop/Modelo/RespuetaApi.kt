package com.example.coffeshop.Modelo

data class RespuestaApi<T>(
    val status: String,
    val message: String,
    val data: T?
)