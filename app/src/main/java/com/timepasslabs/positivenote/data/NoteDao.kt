package com.timepasslabs.positivenote.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Singleton

@Dao
@Singleton
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note : Note)

    @Query("Select * from notes ORDER BY date DESC")
    fun getAllNotes() : Flow<List<Note>>

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNote(id : Int) : Note

    @Delete
    suspend fun deleteNoteList(notes : List<Note>)

}