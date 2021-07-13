package com.timepasslabs.positivenote.data

import android.app.Application
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.timepasslabs.positivenote.DateUtil
import javax.inject.Singleton

private const val TAG = "NoteDatabase"

@Singleton
@Database(entities = [Note::class],version = 15, exportSchema = true)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun getNoteDao() : NoteDao


    companion object {

        private val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE notes ADD COLUMN lastUpdate INTEGER NOT NULL DEFAULT 0"
                )
            }
        }

        private val MIGRATION_2_3 = object : Migration(2,3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE notes ADD COLUMN date TEXT NOT NULL"
                )
            }
        }

        private val Migration_7_8 = object : Migration(14,15) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val cursor = database.query("select * from notes_temp")
                val dateColumnId = cursor.getColumnIndex("date")
                val noteIdColumnId = cursor.getColumnIndex("id")
                database.execSQL("ALTER TABLE `notes` ADD COLUMN `timestamp` INTEGER NOT NULL DEFAULT 0")
                cursor.moveToFirst()
                while(!cursor.isAfterLast) {
                    val oldDate = cursor.getString(dateColumnId)
                    val noteId : Long = cursor.getLong(noteIdColumnId)
                    val newDate : Long = DateUtil.convertOldDateFormatToNew(oldDate)
                    Log.d(TAG, "migrate: id = $noteId old date : $oldDate & new date = $newDate")
                    Log.d(
                        TAG,
                        "migrate: noteId = $noteId, timeStamp = $newDate"
                    )
                    database.execSQL("UPDATE `notes` set timestamp = " + DateUtil.convertOldDateFormatToNew(cursor.getString(cursor.getColumnIndex("date"))) + " WHERE id = " + cursor.getLong(cursor.getColumnIndex("id")))
                    cursor.moveToNext()
                }
                Log.d(TAG, "migrate: end of migration")
                cursor.close()
//                throw Exception("sdk")
            }
        }

        fun createDataBase(application : Application) : NoteDatabase =
            Room.databaseBuilder(application,NoteDatabase::class.java,"note_database.db")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(Migration_7_8)
                .build()
    }

}