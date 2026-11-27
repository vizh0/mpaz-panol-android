package com.example.mpaz_panol.data.remote

import retrofit2.Response
import retrofit2.http.*

interface EquipoApiService {
    @GET("api/v1/equipo")
    suspend fun getEquipos(): Response<List<EquipoDto>>

    @POST("api/v1/equipo")
    suspend fun crearEquipo(@Body equipo: EquipoDto): Response<EquipoDto>

    @PUT("api/v1/equipo/{id}")
    suspend fun actualizarEquipo(@Path("id") id: Long, @Body equipo: EquipoDto): Response<EquipoDto>

    @DELETE("api/v1/equipo/{id}")
    suspend fun eliminarEquipo(@Path("id") id: Long): Response<Unit>
}

interface SolicitudApiService {
    @GET("api/v1/solicitud")
    suspend fun getSolicitudes(): Response<List<SolicitudDto>>

    @POST("api/v1/solicitud")
    suspend fun crearSolicitud(@Body solicitud: SolicitudDto): Response<SolicitudDto>

    @PUT("api/v1/solicitud/{id}")
    suspend fun actualizarSolicitud(@Path("id") id: Long, @Body solicitud: SolicitudDto): Response<SolicitudDto>

    @DELETE("api/v1/solicitud/{id}")
    suspend fun eliminarSolicitud(@Path("id") id: Long): Response<Unit>
}

interface UsuarioApiService {
    @GET("api/v1/usuario")
    suspend fun getUsuarios(): Response<List<UsuarioDto>>

    @POST("api/v1/usuario")
    suspend fun crearUsuario(@Body usuario: UsuarioDto): Response<UsuarioDto>

    @PUT("api/v1/usuario/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Long, @Body usuario: UsuarioDto): Response<UsuarioDto>

    @DELETE("api/v1/usuario/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long): Response<String>
}