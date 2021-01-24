package com.timepasslabs.positivenote.ui.noteDetail

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import com.skydoves.powerspinner.SpinnerGravity
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.data.DateUtil
import kotlinx.android.synthetic.main.fragment_edit_note.*
import java.util.*

private const val NOTE = "note"

private const val TAG = "EditNoteFragment"

class EditNoteFragment : Fragment() {

	private var note: Note? = null

	private val daySelectionList = listOf("today","yesterday","pick date")

	private var currentSelection = 0

	companion object {
		fun newInstance(note: Note?) =
			EditNoteFragment().apply {
				arguments = Bundle().apply {
					putParcelable(NOTE, note)
				}
			}
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		arguments?.let {
			note = it.getParcelable(NOTE)
		}
	}

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View =
		inflater.inflate(R.layout.fragment_edit_note, container, false)

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupSpinner()
		note?.let { populateViews(note!!) }
		if (noteDetails.requestFocus()) {
			if(note != null) {
				val imm =
					requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
				imm.showSoftInput(noteDetails, InputMethodManager.SHOW_IMPLICIT)
			} else {
				requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
			}
		}
	}

	private fun setupSpinner() {
		dateSpinner.apply {
			showArrow = true
			arrowPadding = 4
			arrowResource = R.drawable.ic_arrow_down
			arrowTint = R.color.black
			arrowGravity = SpinnerGravity.END
			showDivider = true
			dividerColor = Color.GRAY
			spinnerPopupBackgroundColor = Color.WHITE
			spinnerPopupWidth = 350
			paint.isUnderlineText = true
			spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
			setItems(daySelectionList)
			setOnSpinnerItemSelectedListener<String> { _, _, newIndex, _ ->
				currentSelection = newIndex
				if(newIndex ==  daySelectionList.size - 1) {
					showCalenderDialog()
				}
				dismiss()
			}
		}
	}

	private fun populateViews(note: Note) {
		noteTitle.setText(note.title)
		noteDetails.setText(note.details)
		dateSpinner.text = DateUtil.getDateForSpinner(note.date!!)
	}

	private fun showCalenderDialog() {
		val calender = Calendar.getInstance()
		val datePickerDialog = DatePickerDialog(
			requireContext(),
			{ _, year, month, dayOfMonth ->
				val dateString = DateUtil.getDateForSpinner(dayOfMonth, month, year)
				dateSpinner.text = dateString
			},
			calender.get(Calendar.YEAR),
			calender.get(Calendar.MONTH),
			calender.get(Calendar.DAY_OF_MONTH)
		)
		datePickerDialog.datePicker.maxDate = Date().time
		datePickerDialog.show()
		datePickerDialog.setCancelable(false)
		datePickerDialog.setCanceledOnTouchOutside(false)
		datePickerDialog.setOnCancelListener {
			Toast.makeText(requireContext(), "Date not set. Resetting to today", Toast.LENGTH_SHORT).show()
			dateSpinner.selectItemByIndex(0)
		}
	}

	fun getNote() : Note? =
		if(noteTitle.text.isNotEmpty()
			|| noteDetails.text.isNotEmpty()) {
			// Updating already existing note or create new note
			note?.let {
				Note(
					noteTitle.text.toString(),
					noteDetails.text.toString(),
					DateUtil.getDateForDb(dateSpinner.text.toString()),
					System.currentTimeMillis(),
					note!!.id
				)
			} ?: Note(
				noteTitle.text.toString(),
				noteDetails.text.toString(),
				DateUtil.getDateForDb(dateSpinner.text.toString()),
				System.currentTimeMillis(),
			)
		} else {
			null
		}
}