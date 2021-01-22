package com.timepasslabs.positivenote

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.timepasslabs.positivenote.ui.noteDetail.NoteDetailActivity
import java.util.*
import kotlin.random.Random

class AlarmReceiver : BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent?) {
		setReminderForNextDay(context)
		val notifManger = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { createNotifChannel(notifManger) }
		val notification = createNotification(context)
		notifManger.notify(Random.nextInt(),notification)
	}

	private fun createNotification(context: Context) : Notification {
		val notificationBuilder = NotificationCompat.Builder(context,NoteAppConstants.NOTIF_CHANNEL_ID)
		notificationBuilder
			.setWhen(System.currentTimeMillis())
			.setSmallIcon(R.drawable.ic_note)
			.setContentTitle("Positive Vibes")
			.setContentText("Make a quick note of the best parts of the day")
			.setContentIntent(getActivityIntent(context))
			.setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
			.setAutoCancel(true)
			.priority = NotificationCompat.PRIORITY_DEFAULT
		return notificationBuilder.build()
	}

	private fun getActivityIntent(context: Context) : PendingIntent {
		val intent = Intent(context,NoteDetailActivity::class.java)
		return TaskStackBuilder.create(context).run {
			addNextIntentWithParentStack(intent)
			getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)!!
		}
	}

	private fun setReminderForNextDay(context: Context) {
		val alarmManger = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val calendar = getCalenderForNextAlarmTime(context)
		val intent = Intent(context,this::class.java)
		val pendingIntent = PendingIntent.getBroadcast(context,0,intent,PendingIntent.FLAG_IMMUTABLE)
		alarmManger.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pendingIntent)
	}

	private fun getCalenderForNextAlarmTime(context: Context) : Calendar {
		val sharedPrefs = context.getSharedPreferences(NoteAppConstants.APP_PREFS,Context.MODE_PRIVATE)
		val hours = sharedPrefs.getInt(NoteAppConstants.TIME_HOURS,0)
		val minutes = sharedPrefs.getInt(NoteAppConstants.TIME_MINUTE,0)
		return Calendar.getInstance().apply {
			timeInMillis = System.currentTimeMillis()
			set(Calendar.HOUR_OF_DAY,hours)
			set(Calendar.MINUTE,minutes)
			set(Calendar.SECOND,0)
			add(Calendar.DATE,1)
		}
	}

	@RequiresApi(Build.VERSION_CODES.O)
	private fun createNotifChannel(notificationManager: NotificationManager) {
		val notificationChannel = NotificationChannel(
			NoteAppConstants.NOTIF_CHANNEL_ID,
			NoteAppConstants.NOTIF_CHANNEL_NAME,
			NotificationManager.IMPORTANCE_DEFAULT
		)
		notificationManager.createNotificationChannel(notificationChannel)
	}

}