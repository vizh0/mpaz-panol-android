package com.example.mpaz_panol.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // TODO: Reemplazar TU_IP_AWS por la IP pública de EC2 (ej: "http://54.123.45.67")
    private const val BASE_HOST = "http://TU_IP_AWS"

    private const val URL_EQUIPOS = "$BASE_HOST:8082/"
    private const val URL_SOLICITUDES = "$BASE_HOST:8083/"
    private const val URL_USUARIOS = "$BASE_HOST:8081/"

    private fun buildRetrofit(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val equiposApi: EquipoApiService by lazy {
        buildRetrofit(URL_EQUIPOS).create(EquipoApiService::class.java)
    }

    val solicitudesApi: SolicitudApiService by lazy {
        buildRetrofit(URL_SOLICITUDES).create(SolicitudApiService::class.java)
    }

    val usuariosApi: UsuarioApiService by lazy {
        buildRetrofit(URL_USUARIOS).create(UsuarioApiService::class.java)
    }
}