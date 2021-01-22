package com.timepasslabs.positivenote.data

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

@SuppressLint("SimpleDateFormat")
object DateUtil {

    private const val TAG = "Utils"

    private val storedDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private val uiDateFormat = SimpleDateFormat("dd/MM/yyyy")

    private const val DATE_DELIMITER = "/"

    fun getDateForDb(uiDate : String) : String {
        val calendar : Calendar
        when(uiDate.toUpperCase()) {
            "TODAY" -> {
                calendar = Calendar.getInstance()
            }
            "YESTERDAY" -> {
                calendar = Calendar.getInstance()
                calendar.add(Calendar.DAY_OF_YEAR, -1)
            }
            else -> {
                calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()
                val dateDetails = uiDate.split(DATE_DELIMITER)
                calendar.set(Calendar.YEAR,dateDetails[2].toInt())
                calendar.set(Calendar.MONTH,dateDetails[1].toInt()-1)
                calendar.set(Calendar.DAY_OF_MONTH,dateDetails[0].toInt())
            }
        }
        return storedDateFormat.format(calendar.time)
    }

    fun getDateForSpinner(storedDate : String) : String {
        val currentDate = Calendar.getInstance()
        val noteDate = Calendar.getInstance()
        noteDate.time = storedDateFormat.parse(storedDate) ?: Date()
        Log.d(TAG, "getDateForSpinner: week of the year is ${currentDate.get(Calendar.WEEK_OF_YEAR)}")
        Log.d(
            TAG,
            "getDateForSpinner: week of the month is ${currentDate.get(Calendar.WEEK_OF_MONTH)}"
        )
        return if(isSameDate(currentDate,noteDate)) {
            "today"
        } else {
            currentDate.add(Calendar.DAY_OF_MONTH, -1)
            return if (isSameDate(currentDate, noteDate)) {
                "yesterday"
            } else {
                uiDateFormat.format(noteDate.time)
            }
        }
    }

    fun getDateForSpinner(dayOfTheMonth : Int,month : Int,year : Int) =
        "$dayOfTheMonth$DATE_DELIMITER${month + 1}$DATE_DELIMITER$year"

    private fun isSameDate(dateOne : Calendar, dateTwo : Calendar) : Boolean {
        val dayOne = dateOne.get(Calendar.DAY_OF_MONTH)
        val monthOne = dateOne.get(Calendar.MONTH)
        val yearOne = dateOne.get(Calendar.YEAR)

        val dayTwo = dateTwo.get(Calendar.DAY_OF_MONTH)
        val monthTwo = dateTwo.get(Calendar.MONTH)
        val yearTwo = dateTwo.get(Calendar.YEAR)

        return dayOne == dayTwo
                && monthOne == monthTwo
                && yearOne == yearTwo
    }
}