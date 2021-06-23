package com.timepasslabs.positivenote

import android.app.AppComponentFactory
import android.app.Application
import com.timepasslabs.positivenote.data.NoteDatabase
import com.timepasslabs.positivenote.data.NoteRepository
import com.timepasslabs.positivenote.di.AppComponent
import com.timepasslabs.positivenote.di.DaggerAppComponent

class NoteApplication : Application() {

	val appComponent by lazy { DaggerAppComponent.factory().create(this) }

}