package com.example.dz3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.bottomsheet.BottomSheetBehavior

class HabitsTabFragment : Fragment() {

    private lateinit var viewModel: HabitViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HabitViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_habits_tab, container, false)

        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)

        val fab: FloatingActionButton = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            showNewFragmentCreateOrEditHabit()
        }

        val adapter = HabitPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Хорошие"
                1 -> "Плохие"
                else -> "Tab ${position + 1}"
            }
        }.attach()

        return view
    }

    private fun showNewFragmentCreateOrEditHabit(position: Int = -1) {
        val bundle = Bundle().apply {
            putBoolean("edit", position != -1)

            if (position != -1) {
                val habit = viewModel.habitListLiveData.value?.get(position)
                putSerializable("key", habit)
                putInt("position", position)
            }
        }

        val fragment = CreateOrEditHabitFragment().apply {
            arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
