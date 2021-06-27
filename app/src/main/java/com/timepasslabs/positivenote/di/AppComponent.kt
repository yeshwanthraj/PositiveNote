package com.timepasslabs.positivenote.di

import android.app.Application
import com.timepasslabs.positivenote.data.NoteRepository
import com.timepasslabs.positivenote.ui.noteDetail.NoteDetailActivity
import com.timepasslabs.positivenote.ui.notelist.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RoomModule::class,AppSubComponents::class])
interface AppComponent {

	@Component.Factory
	interface Factory {
		fun create(@BindsInstance application: Application) : AppComponent
	}

	fun getNoteRepository() : NoteRepository

	fun getNoteDetailComponent() : NoteDetailComponent.Factory

	fun inject(mainActivity: MainActivity)

}