package com.timepasslabs.positivenote.data

import android.annotation.SuppressLint
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@SuppressLint("SimpleDateFormat")
object DateUtil {

    private const val TAG = "Utils"

    private val storedDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    private val uiDateFormat = SimpleDateFormat("dd/MM/yyyy")

    private const val DATE_DELIMITER = "/"

    private val weekDateNumToString  = mapOf(
        1 to "Sunday",
        2 to "Monday",
        3 to "Tuesday",
        4 to "Wednesday",
        5 to "Thursday",
        6 to "Friday",
        7 to "Saturday"
    )

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
        return when(getDaysBetweenDates(currentDate,noteDate)) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> uiDateFormat.format(noteDate.time)
        }
    }

    fun getDateForListItem(storedDate: String) : String {
        val currentDate = Calendar.getInstance()
        val noteDate = Calendar.getInstance()
        noteDate.time = storedDateFormat.parse(storedDate) ?: Date()
        val daysBetweenDates = getDaysBetweenDates(currentDate,noteDate)
        Log.d(TAG, "getDateForListItem: diff $daysBetweenDates")
        return when(daysBetweenDates) {
            0 -> "Today"
            1 -> "Yesterday"
            else -> return if(daysBetweenDates < 7)
                    weekDateNumToString[noteDate.get(Calendar.DAY_OF_WEEK)] ?: error("")
                else
                    uiDateFormat.format(noteDate.time)
        }
    }

    fun getDateForSpinner(dayOfTheMonth : Int,month : Int,year : Int) =
        "$dayOfTheMonth$DATE_DELIMITER${month + 1}$DATE_DELIMITER$year"

    //TODO: this fails if the year is different
    private fun getDaysBetweenDates(currentDate: Calendar, pastDate: Calendar) : Int =
        abs(currentDate.get(Calendar.DAY_OF_YEAR) - pastDate.get(Calendar.DAY_OF_YEAR))

}