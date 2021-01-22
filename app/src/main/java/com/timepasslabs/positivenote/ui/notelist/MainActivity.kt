package com.timepasslabs.positivenote.ui.notelist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timepasslabs.positivenote.PositiveNoteApplication
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.ui.noteDetail.NoteDetailActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

	private val TAG = "MainActivity"

	private val NEW_NOTE_REQUEST_CODE = 100

	private val READ_NOTE_REQUEST_CODE = 101

	private lateinit var recyclerViewAdapter : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>

	private val viewModel : MainActivityViewModel by viewModels {
		MainActivityViewModelFactory((application as PositiveNoteApplication).repository)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		fab.setOnClickListener {
			startActivityForResult(Intent(this, NoteDetailActivity::class.java).apply {
				putExtra(NoteDetailActivity.IS_NEW_NOTE,true)
			},NEW_NOTE_REQUEST_CODE)
		}
		fetchData()
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if(requestCode == NEW_NOTE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
			Toast.makeText(this, "Empty note discarded", Toast.LENGTH_SHORT).show()
		}
	}

	private fun fetchData() {
		viewModel.noteList.observe(this, { noteList ->
			setupRecyclerView(noteList)
		})
	}

	private fun setupRecyclerView(noteList : List<Note>) {
		recyclerViewAdapter = NoteRecyclerViewAdapter(noteList) { note ->
			startActivityForResult(Intent(this,NoteDetailActivity::class.java).apply {
				putExtra(NoteDetailActivity.IS_NEW_NOTE,false)
				putExtra(NoteDetailActivity.NOTE_EXTRA,note)
			},READ_NOTE_REQUEST_CODE)
		}
		noteRecyclerView.adapter = recyclerViewAdapter
		noteRecyclerView.layoutManager = LinearLayoutManager(this)
		noteRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				if(dy > 10) {
					fab.hide()
				} else if(dy < -10) {
					fab.show()
				}
			}
		})
	}

}
