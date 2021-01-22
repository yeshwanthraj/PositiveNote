package com.timepasslabs.positivenote.ui.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.timepasslabs.positivenote.AlarmReceiver
import com.timepasslabs.positivenote.NoteAppConstants
import com.timepasslabs.positivenote.R
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*
import kotlin.math.min

private const val TAG = "ReminderActivity"

class ReminderActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_reminder)
		supportActionBar?.title = "Set reminder"
		setupUi()
	}

	private fun setupUi() {
		done.setOnClickListener {
			val hours = getHours()
			val minutes = getMinutes()
			setUpReminder(hours,minutes)
			storeReminderTime(hours,minutes)
		}
	}

	private fun setUpReminder(hours : Int, minutes : Int) {
		//TODO alarm is triggered immediately
		val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
		val currentTime = System.currentTimeMillis()
		val now = Calendar.getInstance().apply { timeInMillis = currentTime }
		val alarmTime = Calendar.getInstance().apply {
			timeInMillis = currentTime
			set(Calendar.HOUR_OF_DAY,hours)
			set(Calendar.MINUTE,minutes)
			set(Calendar.SECOND,0)
			if(before(now)) {
				add(Calendar.DATE,1)
			}
		}
		val intent = Intent(this,AlarmReceiver::class.java)
		val pendingIntent = PendingIntent.getBroadcast(
			this,
			0,
			intent,
			PendingIntent.FLAG_UPDATE_CURRENT
		)
		alarmManager.setExact(
			AlarmManager.RTC_WAKEUP,
			alarmTime.timeInMillis,
			pendingIntent
		)
		Toast.makeText(this, "Reminder set", Toast.LENGTH_SHORT).show()
	}

	private fun storeReminderTime(hours: Int,minutes: Int) =
		getSharedPreferences(NoteAppConstants.APP_PREFS, MODE_PRIVATE)
			.edit()
			.run {
				putInt(NoteAppConstants.TIME_HOURS,hours)
				putInt(NoteAppConstants.TIME_MINUTE, minutes)
				apply()
			}

	private fun getHours() = timePicker.hour

	private fun getMinutes() = timePicker.minute

}