package com.timepasslabs.positivenote.ui.noteDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.timepasslabs.positivenote.R
import com.timepasslabs.positivenote.DateUtil
import com.timepasslabs.positivenote.data.Note
import com.timepasslabs.positivenote.databinding.FragmentReadOnlyBinding

private val NOTE = "note"

private const val TAG = "ReadOnlyFragment"
class ReadOnlyFragment : Fragment() {

	private lateinit var note: Note
	private lateinit var viewBinding: FragmentReadOnlyBinding

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
	): View {
		viewBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_read_only,container,false)
		viewBinding.note = note
		viewBinding.dateUtil = DateUtil
		return viewBinding.root
	}
}