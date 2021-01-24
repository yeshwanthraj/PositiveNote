package com.timepasslabs.positivenote.ui.noteDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.DateUtil
import kotlinx.android.synthetic.main.fragment_read_only.*

private val NOTE = "note"


class ReadOnlyFragment : Fragment() {

	private lateinit var note: Note

	companion object {
		fun newInstance(note: Note) =
			ReadOnlyFragment().apply {
				arguments = Bundle().apply {
					putParcelable(NOTE, note)
				}
			}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			note = it.getParcelable(NOTE)!!
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.fragment_read_only, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		showNote(note)
	}

	private fun showNote(note : Note) {
		noteTitle.text = note.title
		noteDetails.text = note.details
		noteTitle.visibility = if(note.title.isEmpty()) View.GONE else View.VISIBLE
		noteDetails.visibility = if(note.details.isEmpty()) View.GONE else View.VISIBLE
		dateDesc.text = DateUtil.getDateForSpinner(note.date!!)
	}
}