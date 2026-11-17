package com.example.mpaz_panol.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

object RutUtils {
    fun isValidRut(rut: String): Boolean {
        val clean = rut.replace(".", "").replace("-", "").uppercase()
        if (clean.length < 8) return false
        val body = clean.dropLast(1)
        val dv = clean.last()
        if (!body.all { it.isDigit() }) return false

        var sum = 0
        var multiplier = 2
        for (i in body.reversed()) {
            sum += i.digitToInt() * multiplier
            multiplier = if (multiplier == 7) 2 else multiplier + 1
        }
        val expected = when (val mod = 11 - (sum % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> mod.digitToChar()
        }
        return dv == expected
    }

    /**
     * Regla Crítica 3.3 del Caso MPAZ:
     * Exige un mínimo de 3 horas de anticipación respecto al horario de uso programado.
     */
    fun validateThreeHoursAnticipation(dateStr: String, startTimeStr: String): ValidationResult {
        return try {
            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val date = LocalDate.parse(dateStr, dateFormatter)
            val time = LocalTime.parse(startTimeStr, timeFormatter)
            val scheduledDateTime = LocalDateTime.of(date, time)
            val now = LocalDateTime.now()

            val hoursDiff = ChronoUnit.MINUTES.between(now, scheduledDateTime) / 60.0
            if (hoursDiff < 3.0) {
                ValidationResult.Error("Regla del Pañol: Debes solicitar con al menos 3 horas de anticipación.")
            } else {
                ValidationResult.Success
            }
        } catch (e: Exception) {
            ValidationResult.Error("Fecha u hora inválida. Usa formato dd/MM/yyyy y HH:mm.")
        }
    }
}