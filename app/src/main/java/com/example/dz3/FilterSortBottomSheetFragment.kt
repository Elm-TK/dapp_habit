package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterSortBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var viewModel: HabitViewModel
    private lateinit var searchEditText: SearchView
    private lateinit var sortButton: Button
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
        sortButton = view.findViewById(R.id.sortButton)

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

        sortButton.setOnClickListener {
            viewModel.sortHabitsByDate()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.peekHeight = 800

        return view
    }
}
