package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

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
        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]
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
                habitAdapter.updateData(habits)
            }

            HabitType.BAD -> viewModel.badHabitsLiveData.observe(viewLifecycleOwner) { habits ->
                habitAdapter.updateData(habits)
            }
        }

        return view
    }

    private fun showNewFragmentCreateOrEditHabit(id: Int = -1) {
        val bundle = Bundle().apply {
            putBoolean(CreateOrEditHabitFragment.ARG_IS_EDIT, id != -1)

            if (id != -1) {
                val habit = viewModel.getHabitById(id)
                putSerializable(CreateOrEditHabitFragment.ARG_HABIT, habit)
            }
        }

        val fragment = CreateOrEditHabitFragment().apply {
            arguments = bundle
        }

        requireParentFragment().parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
