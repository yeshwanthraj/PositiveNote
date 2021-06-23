package com.timepasslabs.positivenote.data

import android.annotation.SuppressLint
import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat")
object DateTypeConvertor {

	private val dateFormat = "yyyy-MM-dd HH:mm:ss"

	@TypeConverter
	@JvmStatic
	fun toCalender(formattedDate: String): Calendar {
		val calender = Calendar.getInstance()
		calender.time = SimpleDateFormat(dateFormat).parse(formattedDate) ?: Date()
		return calender
	}

	@TypeConverter
	@JvmStatic
	fun toFormattedDateString(calender : Calendar) : String {
		val date = calender.time
		return SimpleDateFormat(dateFormat).format(date)
	}

}