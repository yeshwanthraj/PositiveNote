package com.timepasslabs.positivenote.ui.noteDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.timepasslabs.positivenote.PositiveNoteApplication
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.data.Note
import kotlinx.coroutines.launch

private const val TAG = "NewNoteActivity"

class NoteDetailActivity : AppCompatActivity() {

	companion object {
		val NOTE_EXTRA = "note"
		val IS_NEW_NOTE = "isNewNote"
	}

	private var isReadOnly = true

	private var isNewNote = false

	private var note : Note? = null

	private lateinit var currentFragment : Fragment

	private val viewModel : NoteDetailViewModel by viewModels {
		NoteDetailViewModelFactory((application as PositiveNoteApplication).repository)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_new_note)
		isNewNote = intent.getBooleanExtra(IS_NEW_NOTE,false)
		supportActionBar?.setDisplayHomeAsUpEnabled(true)
		supportActionBar?.setDisplayShowHomeEnabled(true)
		isReadOnly = !isNewNote
		note = intent.getParcelableExtra(NOTE_EXTRA)
		currentFragment = if(isNewNote) {
			EditNoteFragment.newInstance(note)
		} else (
				ReadOnlyFragment.newInstance(note!!)
				)
		supportActionBar!!.title = if(isNewNote) "New entry" else ""
		supportFragmentManager.beginTransaction().run {
			add(R.id.fragmentContainer,currentFragment)
		}.commit()
	}

	override fun onBackPressed() {
		if(!isReadOnly) {
			saveNote()
		} else {
			setResult(RESULT_OK)
			finish()
		}
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		if(isReadOnly)
			menuInflater.inflate(R.menu.menu_view_note,menu)
		else
			menuInflater.inflate(R.menu.menu_new_note,menu)
		return true
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when(item.itemId) {
			android.R.id.home -> {
				onBackPressed()
			}
			R.id.save -> {
				saveNote()
			}
			R.id.delete -> {
				lifecycleScope.launch {
					viewModel.noteRepository.deleteNote(note!!)
					setResult(RESULT_OK)
					finish()
				}
			}
			R.id.edit -> {
				isReadOnly = false
				invalidateOptionsMenu()
				currentFragment = EditNoteFragment.newInstance(note)
				supportFragmentManager.beginTransaction().apply {
					replace(R.id.fragmentContainer,currentFragment)
				}.commit()
			}
		}
		return true
	}

	private fun showDeleteNoteDialog() {
		val dialog =
			AlertDialog.Builder(this)
				.setMessage(R.string.confirm_delete)
				.setPositiveButton(R.string.delete) { _, _ ->
					lifecycleScope.launch {
						viewModel.noteRepository.deleteNote(note!!)
						setResult(RESULT_OK)
						finish()
					}
				}
				.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
					dialogInterface.dismiss()
				}
		dialog.setCancelable(false)
		dialog.show()
	}

	private fun saveNote() {
		val currentNote = (currentFragment as EditNoteFragment).getNote()
		currentNote?.let {
			lifecycleScope.launch {
				viewModel.noteRepository.addNote(currentNote)
				setResult(RESULT_OK)
				finish()
			}
		} ?: run {
			if(!isNewNote) {
				showDeleteNoteDialog()
			} else {
				setResult(RESULT_CANCELED)
				finish()
			}
		}
	}
}
