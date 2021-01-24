package com.timepasslabs.positivenote.ui.notelist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.DateUtil

private const val TAG = "NoteRecyclerViewAdapter"

class NoteRecyclerViewAdapter(
	private val noteList : List<Note>,
	private val clickListener : (Note) -> Unit,
) : RecyclerView.Adapter<NoteRecyclerViewAdapter.NoteViewHolder>() {

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note_entry,parent,false)
		return NoteViewHolder(view)
	}

	override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
		holder.bindData(noteList[position])
	}

	override fun getItemCount(): Int = noteList.size

	inner class NoteViewHolder(view : View) : RecyclerView.ViewHolder(view) {

		private val title = view.findViewById<TextView>(R.id.title)

		private val desc = view.findViewById<TextView>(R.id.note)

		private val date = view.findViewById<TextView>(R.id.date)

		init {
			view.setOnClickListener {
				clickListener.invoke(noteList[adapterPosition])
			}
		}

		fun bindData(note : Note) {
			Log.d(TAG, "bindData: note is $note")
			title.text = note.title
			desc.text = note.details
			title.visibility = if(note.title.isEmpty()) View.GONE else View.VISIBLE
			desc.visibility = if(note.details.isEmpty()) View.GONE else View.VISIBLE
			Log.d(TAG, "bindData: visibility of title : ${title.visibility} and ${desc.visibility}")
			date.text = DateUtil.getDateForListItem(note.date)
		}
	}
}