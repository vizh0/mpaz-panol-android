package com.example.mpaz_panol.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.CalendarContract
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object NativeResourcesHelper {

    /**
     * RECURSO NATIVO 1: Integración con la App de Calendario nativa de Android
     */
    fun exportToNativeCalendar(
        context: Context,
        title: String,
        location: String,
        description: String,
        dateStr: String,
        startTimeStr: String,
        endTimeStr: String
    ) {
        try {
            val date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val start = LocalTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            val end = LocalTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"))

            val startMillis = date.atTime(start).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val endMillis = date.atTime(end).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, "Préstamo Pañol MPAZ: $title")
                putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (_: Exception) { }
    }

    /**
     * RECURSO NATIVO 2: Motor de Vibración (Vibrator Service)
     */
    @SuppressLint("MissingPermission")
    fun triggerVibration(context: Context, isError: Boolean = false) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (isError) {
                vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 100, 80, 100), -1))
            } else {
                vibrator.vibrate(VibrationEffect.createOneShot(120, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } else {
            @Suppress("DEPRECATION")
            if (isError) {
                vibrator.vibrate(longArrayOf(0, 100, 80, 100), -1)
            } else {
                vibrator.vibrate(120)
            }
        }
    }
}