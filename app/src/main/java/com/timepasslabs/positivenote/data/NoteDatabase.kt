package com.timepasslabs.positivenote.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Note::class],version = 3)
abstract class NoteDatabase() : RoomDatabase() {

    abstract fun getNoteDao() : NoteDao

    companion object {

        @Volatile private var INSTANCE : NoteDatabase? = null

        fun getInstance(context : Context) : NoteDatabase =
            INSTANCE?: synchronized(this) {
                INSTANCE?: buildDatabase(context).also { INSTANCE = it }
            }

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

        private fun buildDatabase(context : Context) : NoteDatabase =
            Room.databaseBuilder(context,NoteDatabase::class.java,"note_database.db")
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()

    }

}