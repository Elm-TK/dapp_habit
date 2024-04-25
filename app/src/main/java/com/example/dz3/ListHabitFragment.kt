package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class ListHabitFragment : Fragment() {

    private lateinit var viewModel: HabitViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var habitAdapter: HabitAdapter

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        const val ARG_HABIT_TYPE = "habit-type"

        fun newInstance(columnCount: Int, habitType: HabitType) = ListHabitFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_COLUMN_COUNT, columnCount)
                putSerializable(ARG_HABIT_TYPE, habitType)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment().requireParentFragment())[HabitViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.habits_list, container, false)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)

        habitAdapter = HabitAdapter(emptyList()) { position ->
            showNewFragmentCreateOrEditHabit(position)
        }
        recyclerView.adapter = habitAdapter

        val habitType = arguments?.getSerializable(ARG_HABIT_TYPE)
        when (habitType) {
            HabitType.GOOD -> viewModel.goodHabitsLiveData.observe(viewLifecycleOwner) { habits ->
                habits?.let {
                    habitAdapter.updateData(it)
                }
            }

            HabitType.BAD -> viewModel.badHabitsLiveData.observe(viewLifecycleOwner) { habits ->
                habits?.let {
                    habitAdapter.updateData(it)
                }
            }
        }

        return view
    }


    private fun showNewFragmentCreateOrEditHabit(id: Long = -1) {
        if (id != -1L) {
            lifecycleScope.launch {
                val habit = viewModel.getHabitById(id)
                val bundle = Bundle().apply {
                    putBoolean(CreateOrEditHabitFragment.ARG_IS_EDIT, true)
                    putSerializable(CreateOrEditHabitFragment.ARG_HABIT, habit)
                }
                navigateToEditFragment(bundle)
            }
        } else {
            val bundle = Bundle().apply {
                putBoolean(CreateOrEditHabitFragment.ARG_IS_EDIT, false)
            }
            navigateToEditFragment(bundle)
        }
    }

    private fun navigateToEditFragment(bundle: Bundle) {
        val fragment = CreateOrEditHabitFragment().apply {
            arguments = bundle
        }
        requireParentFragment().parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
