package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

private const val ARG_PARAM1 = "key"

class CreateOrEditHabitFragment : Fragment() {

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
            habit = arguments?.getSerializable(ARG_PARAM1) as? Habit
            position = arguments?.getInt("position") ?: 0
            isEdit = arguments?.getBoolean("edit") ?: false
        }

        viewModel = ViewModelProvider(requireActivity())[HabitViewModel::class.java]
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

        if (isEdit) {
            view.findViewById<TextView>(R.id.action_title).text = "Изменение привычки"
            button.text = "Изменить"
            titleInput.setText(habit?.title)
            descriptionInput.setText(habit?.description)
            repeatInput.setText(habit?.repeat.toString())
            daysInput.setText(habit?.days.toString())
            prioritySelector.setSelection(getIndex(prioritySelector, habit?.priority ?: ""))
            radioGroup.children.forEach { radioButton ->
                if ((radioButton as RadioButton).text == habit?.type) {
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
                val priority: String = prioritySelector.selectedItem.toString()
                val type: String = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
                val repeat: Int = repeatInput.text.toString().toInt()
                val days: Int = daysInput.text.toString().toInt()

                val newHabit = Habit(1, 1, title, description, priority, type, repeat, days)

                if (isEdit) {
                    habit?.let {
                        viewModel.updateHabit(it.id, newHabit)
                    }
                } else {
                    viewModel.addHabit(newHabit)
                }

                parentFragmentManager.popBackStack()
            }
        }
    }


    private fun isValidInput(): Boolean {
        var isValid = true

        if (titleInput.text.isNullOrBlank()) {
            titleInput.setError("Введите название привычки")
            isValid = false
        }

        if (repeatInput.text.isNullOrBlank() || repeatInput.text.toString()
                .toIntOrNull() == null || repeatInput.text.toString() == "0"
        ) {
            repeatInput.setError("Введите корректное число повторений")
            isValid = false
        }

        if (daysInput.text.isNullOrBlank() || daysInput.text.toString()
                .toIntOrNull() == null || daysInput.text.toString() == "0"
        ) {
            daysInput.setError("Введите корректное число дней")
            isValid = false
        }

        return isValid
    }

    private fun getIndex(spinner: Spinner, myString: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i) == myString) {
                return i
            }
        }
        return 0
    }
}
