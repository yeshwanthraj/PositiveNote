package com.timepasslabs.positivenote.di

import com.timepasslabs.positivenote.ui.noteDetail.EditNoteFragment
import com.timepasslabs.positivenote.ui.noteDetail.NoteDetailActivity
import com.timepasslabs.positivenote.ui.noteDetail.ReadOnlyFragment
import dagger.Subcomponent

@NoteDetailScope
@Subcomponent
interface NoteDetailComponent {

	@Subcomponent.Factory
	interface Factory {
		fun create() : NoteDetailComponent
	}

	fun inject(noteDetailActivity: NoteDetailActivity)

	fun inject(editNoteFragment: EditNoteFragment)

	fun inject(readOnlyFragment: ReadOnlyFragment)

}