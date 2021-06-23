package com.timepasslabs.positivenote.di

import android.app.Application
import android.content.Context
import com.timepasslabs.positivenote.data.NoteDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {

	@Singleton
	@Provides
	fun providesNoteDao(application: Application) =
		NoteDatabase.createDataBase(application).getNoteDao()

}