package com.example.dz3.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dz3.*
import com.example.dz3.db.HabitDatabase
import com.example.dz3.db.HabitRepository
import com.example.dz3.models.Habit
import com.example.dz3.models.HabitPriority
import com.example.dz3.models.HabitType
import com.example.dz3.view_models.CreateOrEditHabitViewModel
import com.example.dz3.view_models.CreateOrEditHabitViewModelFactory

class CreateOrEditHabitFragment : Fragment() {

    companion object {
        const val ARG_HABIT = "key"
        const val ARG_POSITION = "position"
        const val ARG_IS_EDIT = "edit"
    }

    private var habit: Habit? = null
    private var position: Int = 0
    private var isEdit: Boolean = false

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var repeatInput: EditText
    private lateinit var daysInput: EditText
    private lateinit var prioritySelector: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var button: Button
    private lateinit var delButton: Button
    private lateinit var doneButton: Button

    private val viewModel: CreateOrEditHabitViewModel by viewModels {
        val habitDao = HabitDatabase.getInstance(requireContext()).habitDao()
        val repository = HabitRepository(habitDao)
        CreateOrEditHabitViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habit = arguments?.getSerializable(ARG_HABIT) as? Habit
            position = arguments?.getInt(ARG_POSITION) ?: 0
            isEdit = arguments?.getBoolean(ARG_IS_EDIT) ?: false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_or_edit_habit, container, false)

        titleInput = view.findViewById(R.id.title_input)
        descriptionInput = view.findViewById(R.id.description_input)
        repeatInput = view.findViewById(R.id.repeat_input)
        daysInput = view.findViewById(R.id.days_input)
        prioritySelector = view.findViewById(R.id.priority_selector)
        radioGroup = view.findViewById(R.id.radioGroup)
        button = view.findViewById(R.id.button)
        delButton = view.findViewById(R.id.delete)
        doneButton = view.findViewById(R.id.done)

        val priorities = resources.getStringArray(R.array.priorities)
        val priorityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, priorities)
        prioritySelector.adapter = priorityAdapter

        if (isEdit) {
            view.findViewById<TextView>(R.id.action_title).text = getString(R.string.edit_habit_title)
            button.text = getString(R.string.edit_button_text)
            titleInput.setText(habit?.title)
            descriptionInput.setText(habit?.description)
            repeatInput.setText(habit?.repeat.toString())
            daysInput.setText(habit?.days.toString())

            prioritySelector.setSelection(
                when (habit?.priority) {
                    HabitPriority.HIGH -> 0
                    HabitPriority.MEDIUM -> 1
                    HabitPriority.LOW -> 2
                    else -> 1
                }
            )
            radioGroup.children.forEach { radioButton ->
                if ((radioButton as RadioButton).text == mapTypeToText(habit?.type)) {
                    radioButton.isChecked = true
                }
            }
        }

        viewModel.habitSavedEvent.observe(viewLifecycleOwner) { event ->
            if (event) {
                parentFragmentManager.popBackStack()
            }
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        doneButton.setOnClickListener {
            viewModel.markHabitDone(habit)
        }

        delButton.setOnClickListener {
            viewModel.deleteHabit(habit)
        }

        button.setOnClickListener {
            val title: String = titleInput.text.toString()
            val description: String = descriptionInput.text.toString()
            val repeat: String = repeatInput.text.toString()
            val days: String = daysInput.text.toString()

            if (viewModel.isValidInput(title, repeat, days)) {
                val priority: HabitPriority = when (prioritySelector.selectedItem.toString()) {
                    getString(R.string.high_priority) -> HabitPriority.HIGH
                    getString(R.string.medium_priority) -> HabitPriority.MEDIUM
                    getString(R.string.low_priority) -> HabitPriority.LOW
                    else -> HabitPriority.MEDIUM
                }

                val type: HabitType =
                    when (radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()) {
                        getString(R.string.good_habit) -> HabitType.GOOD
                        getString(R.string.bad_habit) -> HabitType.BAD
                        else -> HabitType.GOOD
                    }

                viewModel.saveHabit(
                    habit,
                    isEdit,
                    title,
                    description,
                    repeat.toInt(),
                    days.toInt(),
                    priority,
                    type
                )
            } else {
                if (title.isBlank()) titleInput.error = getString(R.string.enter_habit_title)
                if (repeat.isBlank() || repeat.toIntOrNull() == null || repeat == "0") repeatInput.error =
                    getString(R.string.enter_valid_repeat)
                if (days.isBlank() || days.toIntOrNull() == null || days == "0") daysInput.error =
                    getString(R.string.enter_valid_days)
            }
        }
    }

    private fun mapTypeToText(type: HabitType?): String {
        return when (type) {
            HabitType.GOOD -> getString(R.string.good_habit)
            HabitType.BAD -> getString(R.string.bad_habit)
            else -> getString(R.string.good_habit)
        }
    }
}
