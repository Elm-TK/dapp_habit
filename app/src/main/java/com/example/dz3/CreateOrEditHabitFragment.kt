package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CreateOrEditHabitFragment : Fragment() {

    companion object {
        const val ARG_HABIT = "key"
        const val ARG_POSITION = "position"
        const val ARG_IS_EDIT = "edit"
    }

    private var habit: Habit? = null
    private var position: Int = 0
    private var isEdit: Boolean = false
    private lateinit var viewModel: HabitViewModel

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var repeatInput: EditText
    private lateinit var daysInput: EditText
    private lateinit var prioritySelector: Spinner
    private lateinit var radioGroup: RadioGroup
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            habit = arguments?.getSerializable(ARG_HABIT) as? Habit
            position = arguments?.getInt(ARG_POSITION) ?: 0
            isEdit = arguments?.getBoolean(ARG_IS_EDIT) ?: false
        }

        viewModel = ViewModelProvider(requireParentFragment())[HabitViewModel::class.java]
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            if (isValidInput()) {
                val title: String = titleInput.text.toString()
                val description: String = descriptionInput.text.toString()
                val repeat: Int = repeatInput.text.toString().toInt()
                val days: Int = daysInput.text.toString().toInt()

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

                val newHabit = Habit(0, title, description, priority, type, repeat, days)



                lifecycleScope.launch {
                    if (isEdit) {
                        habit?.let {
                            newHabit.id = habit!!.id
                            viewModel.updateHabit(newHabit)
                        }
                    } else {
                        viewModel.addHabit(newHabit)
                    }
                }

                parentFragmentManager.popBackStack()
            }
        }
    }

    private fun isValidInput(): Boolean {
        var isValid = true

        if (titleInput.text.isNullOrBlank()) {
            titleInput.setError(getString(R.string.enter_habit_title))
            isValid = false
        }

        if (repeatInput.text.isNullOrBlank() || repeatInput.text.toString()
                .toIntOrNull() == null || repeatInput.text.toString() == "0"
        ) {
            repeatInput.setError(getString(R.string.enter_valid_repeat))
            isValid = false
        }

        if (daysInput.text.isNullOrBlank() || daysInput.text.toString()
                .toIntOrNull() == null || daysInput.text.toString() == "0"
        ) {
            daysInput.setError(getString(R.string.enter_valid_days))
            isValid = false
        }

        return isValid
    }

    private fun mapTypeToText(type: HabitType?): String {
        return when (type) {
            HabitType.GOOD -> getString(R.string.good_habit)
            HabitType.BAD -> getString(R.string.bad_habit)
            else -> getString(R.string.good_habit)
        }
    }
}
