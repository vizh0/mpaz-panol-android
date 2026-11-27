package com.example.mpaz_panol.data.repository

import com.example.mpaz_panol.data.local.dao.ItemDao
import com.example.mpaz_panol.data.local.dao.RequestDao
import com.example.mpaz_panol.data.local.entity.RequestEntity
import com.example.mpaz_panol.data.remote.RetrofitClient
import com.example.mpaz_panol.data.remote.SolicitudDto
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RequestRepository(
    private val requestDao: RequestDao,
    private val itemDao: ItemDao
) {
    fun getRequestsForUser(userId: Int): Flow<List<RequestEntity>> = requestDao.getRequestsByUser(userId)
    fun getAllRequests(): Flow<List<RequestEntity>> = requestDao.getAllRequests()

    suspend fun createRequest(request: RequestEntity) {
        requestDao.insertRequest(request)
        try {
            val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(request.createdAt))
            RetrofitClient.solicitudesApi.crearSolicitud(
                SolicitudDto(
                    usuarioId = request.userId.toLong(),
                    salaUtilizacion = request.room,
                    fechaPlanificada = "${request.date} ${request.startTime}-${request.endTime}",
                    estadoTicket = request.status,
                    fechaCreacion = hoy,
                    comentarioGestion = request.itemsSummary
                )
            )
        } catch (_: Exception) { }
    }

    suspend fun updateRequestStatus(request: RequestEntity, newStatus: String, reason: String = "") {
        val updated = request.copy(status = newStatus, rejectionReason = reason)
        requestDao.updateRequest(updated)

        try {
            if (request.id > 0) {
                val hoy = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(request.createdAt))
                RetrofitClient.solicitudesApi.actualizarSolicitud(
                    request.id.toLong(),
                    SolicitudDto(
                        id = request.id.toLong(),
                        usuarioId = request.userId.toLong(),
                        salaUtilizacion = request.room,
                        fechaPlanificada = "${request.date} ${request.startTime}-${request.endTime}",
                        estadoTicket = newStatus,
                        fechaCreacion = hoy,
                        comentarioGestion = reason.ifBlank { request.itemsSummary }
                    )
                )
            }
        } catch (_: Exception) { }

        // Sincronización inmediata de inventario (Regla de éxito 4.1 del caso)
        val pairs = request.itemsData.split(",").filter { it.contains(":") }
        for (pair in pairs) {
            val (idStr, qtyStr) = pair.split(":")
            val item = itemDao.getItemById(idStr.toInt()) ?: continue
            val qty = qtyStr.toInt()

            when (newStatus) {
                "Aceptada" -> {
                    val newAvail = (item.availableStock - qty).coerceAtLeast(0)
                    itemDao.updateItem(
                        item.copy(
                            availableStock = newAvail,
                            status = if (newAvail == 0) "Reservado" else item.status
                        )
                    )
                }
                "Devuelta" -> {
                    val newAvail = (item.availableStock + qty).coerceAtMost(item.totalStock)
                    itemDao.updateItem(
                        item.copy(
                            availableStock = newAvail,
                            status = "Disponible"
                        )
                    )
                }
            }
        }
    }
}