package com.timepasslabs.positivenote.ui.noteDetail

import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import com.skydoves.powerspinner.SpinnerGravity
import com.timepasslabs.positivenote.CustomViewModelFactory
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.DateUtil
import com.timepasslabs.positivenote.databinding.FragmentEditNoteBinding
import com.timepasslabs.positivenote.extensions.toast

import java.util.*
import javax.inject.Inject

private const val NOTE = "note"

private const val TAG = "EditNoteFragment"

class EditNoteFragment : Fragment() {

	private var note: Note? = null

	private val daySelectionList = listOf("today","yesterday","pick date")

	@Inject lateinit var viewModelFactory : CustomViewModelFactory

	private lateinit var selectedDate : String

	private val noteDetailViewModel by lazy {
		ViewModelProvider(requireActivity(),viewModelFactory).get(NoteDetailViewModel::class.java)
	}

	private lateinit var viewBinding : FragmentEditNoteBinding

	private lateinit var noteDetailCallback: NoteDetailCallback

	companion object {
		fun newInstance(note: Note?) =
			EditNoteFragment().apply {
				arguments = Bundle().apply {
					putParcelable(NOTE, note)
				}
			}
	}

	override fun onAttach(context: Context) {
		super.onAttach(context)
		(requireActivity() as NoteDetailActivity).noteDetailComponent.create().inject(this)
		noteDetailCallback = context as NoteDetailCallback
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
	): View {
		viewBinding = FragmentEditNoteBinding.inflate(inflater,container,false)
		return viewBinding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupSpinner()
		viewBinding.apply {
			if(note != null) {
				noteDetails.setText(note!!.details)
				noteTitle.setText(note!!.title)
				dateSpinner.text = DateUtil.getDateForSpinner(note!!.timestamp)
			}
		}
		if (viewBinding.noteDetails.requestFocus()) {
			if(note != null) {
				val imm =
					requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
				imm.showSoftInput(viewBinding.noteDetails, InputMethodManager.SHOW_IMPLICIT)
			} else {
				requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
			}
		}
	}

	fun updateNote() = viewBinding.run {
		if(note != null) {
			if(noteTitle.text.isNotEmpty()
				|| noteDetails.text.isNotEmpty()) {
				// Updating already existing note or create new note
				note = note!!.apply {
					title = noteTitle.text.toString()
					details = noteDetails.text.toString()
					timestamp = DateUtil.getDateForDb(dateSpinner.text.toString())
					lastUpdate = System.currentTimeMillis()
				}
				noteDetailViewModel.addNote(note!!)
				noteDetailCallback.onNoteUpdated()
			} else {
				showDeleteNoteDialog()
			}
		} else {
			if(noteTitle.text.isEmpty() && noteDetails.text.isEmpty()) {
				requireContext().toast(R.string.empty_note)
			} else {
				note = Note(noteTitle.text.toString(),
					noteDetails.text.toString(),
					DateUtil.getDateForDb(dateSpinner.text.toString()),
					System.currentTimeMillis())
				noteDetailViewModel.addNote(note!!)
			}
			noteDetailCallback.onNoteUpdated()
		}
	}

	private fun showDeleteNoteDialog() {
		val dialog =
			AlertDialog.Builder(requireContext())
				.setMessage(R.string.note_cleared)
				.setPositiveButton(R.string.delete) { _, _ ->
					note?.let { noteDetailViewModel.deleteNote(it) }
					noteDetailCallback.onNoteUpdated()
				}
				.setNegativeButton(R.string.cancel) { dialogInterface, _ ->
					dialogInterface.dismiss()
				}
		dialog.setCancelable(false)
		dialog.show()
	}

	private fun setupSpinner() = viewBinding.dateSpinner.apply {
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
			if (newIndex == daySelectionList.size - 1) {
				showCalenderDialog()
			} else {
				selectedDate = daySelectionList[newIndex]
			}
			dismiss()
		}
		selectedDate = text.toString()
	}

	private fun showCalenderDialog() {
		val calender = Calendar.getInstance()
		val datePickerDialog = DatePickerDialog(
			requireContext(),
			{ _, year, month, dayOfMonth ->
				selectedDate = DateUtil.getDateForSpinner(dayOfMonth, month, year)
				viewBinding.dateSpinner.text = selectedDate
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
			if(note != null) {
				viewBinding.dateSpinner.text = DateUtil.getDateForSpinner(note!!.timestamp)
			} else {
				viewBinding.dateSpinner.text = selectedDate
			}
		}
	}

	interface NoteDetailCallback {
		fun onNoteUpdated()
	}
}