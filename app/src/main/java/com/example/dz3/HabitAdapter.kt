package com.example.dz3

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
            priorityView.text = habit.priority
            typeView.text = habit.type
            periodView.text = "${habit.repeat} раз в ${habit.days} дней"
        }
    }

    private fun onItemClick(id: Int) {
        showNewFragmentCreateOrEditHabit(id)
    }
}
