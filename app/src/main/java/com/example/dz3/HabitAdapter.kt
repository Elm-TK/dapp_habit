package com.example.dz3

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dz3.databinding.HabitBinding

class HabitAdapter(
    private var habitList: List<Habit>,
    private val showNewFragmentCreateOrEditHabit: (Int) -> Unit
) : RecyclerView.Adapter<HabitAdapter.ViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = HabitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentHabit = habitList[position]
        holder.bind(currentHabit)
    }

    override fun getItemCount(): Int = habitList.size

    fun updateData(newData: List<Habit>) {
        habitList = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: HabitBinding) : RecyclerView.ViewHolder(binding.root),
        View.OnClickListener {

        private val colorView: ImageView = binding.colorImageView
        private val titleView: TextView = binding.titleTextView
        private val descriptionView: TextView = binding.descriptionTextView
        private val priorityView: TextView = binding.priorityTextView
        private val typeView: TextView = binding.typeTextView
        private val periodView: TextView = binding.periodTextView

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val id = habitList[bindingAdapterPosition].id
            if (id != RecyclerView.NO_ID.toInt()) {
                onItemClick(id)
            }
        }

        fun bind(habit: Habit) {
            colorView.setImageResource(R.drawable.circle_green_24)
            titleView.text = habit.title
            descriptionView.text = habit.description
            priorityView.text = mapPriorityToText(habit.priority)
            typeView.text = mapTypeToText(habit.type)
            periodView.text =
                "${habit.repeat} ${
                    context.getString(
                        R.string.times_in,
                        habit.repeat,
                        habit.days
                    )
                } ${habit.days} ${context.getString(R.string.week, habit.repeat, habit.days)}"
        }

        private fun mapPriorityToText(priority: HabitPriority): String {
            return when (priority) {
                HabitPriority.HIGH -> context.getString(R.string.high_priority)
                HabitPriority.MEDIUM -> context.getString(R.string.medium_priority)
                HabitPriority.LOW -> context.getString(R.string.low_priority)
            }
        }

        private fun mapTypeToText(type: HabitType): String {
            return when (type) {
                HabitType.GOOD -> context.getString(R.string.good_habit)
                HabitType.BAD -> context.getString(R.string.bad_habit)
            }
        }
    }

    private fun onItemClick(id: Int) {
        showNewFragmentCreateOrEditHabit(id)
    }
}
