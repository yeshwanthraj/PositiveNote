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
@Database(entities = [Note::class],version = 7, exportSchema = true)
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

        private val Migration_6_7 = object : Migration(6,7) {
            override fun migrate(database: SupportSQLiteDatabase) {
                val cursor = database.query("Select * from notes")
                val dateColumnId = cursor.getColumnIndex("date")
                val noteIdColumnId = cursor.getColumnIndex("id")
                Log.d(TAG, "migrate: dateColumnId and noteIdColumnId are $dateColumnId and $noteIdColumnId")
                cursor.moveToFirst()
                while(!cursor.isAfterLast) {
                    val oldDate = cursor.getString(dateColumnId)
                    val noteId = cursor.getInt(noteIdColumnId)
                    val newDate = DateUtil.convertOldDateFormatToNew(oldDate)
                    Log.d(TAG, "migrate: date columnId. old date = $oldDate , newDate = $newDate")
                    Log.d(TAG, "migrate: id index is $noteIdColumnId")
                    database.execSQL("UPDATE notes SET date = '$newDate' WHERE id = $noteId")
                    cursor.moveToNext()
                }
                Log.d(TAG, "migrate: end of migration")
                cursor.close()
            }
        }

        fun createDataBase(application : Application) : NoteDatabase =
            Room.databaseBuilder(application,NoteDatabase::class.java,"note_database.db")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .addMigrations(Migration_6_7)
                .build()
    }

}