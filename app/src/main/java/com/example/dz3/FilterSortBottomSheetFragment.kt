package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterSortBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: HabitViewModel
    private lateinit var searchEditText: SearchView
    private lateinit var sortSpinner: Spinner
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    companion object {
        fun newInstance(bottomSheetBehavior: BottomSheetBehavior<View>): FilterSortBottomSheetFragment {
            val fragment = FilterSortBottomSheetFragment()
            fragment.bottomSheetBehavior = bottomSheetBehavior
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_filter_sort_bottom_sheet, container, false)

        searchEditText = view.findViewById(R.id.searchEditText)
        sortSpinner = view.findViewById(R.id.sortSpinner)

        val sortOptions = arrayOf((getString(R.string.sortInc_button_text)), (getString(R.string.sortDes_button_text)))
        val sortAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sortOptions)
        sortSpinner.adapter = sortAdapter

        searchEditText.queryHint = getString(R.string.filter_hint)

        searchEditText.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    viewModel.filterHabits(newText)
                }
                return true
            }
        })

        sortSpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> viewModel.sortHabitsByPriorityDescending()
                    1 -> viewModel.sortHabitsByPriorityAscending()
                }
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 800

        return view
    }
}
