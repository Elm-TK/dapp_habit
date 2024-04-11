package com.example.dz3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HabitsTabFragment : Fragment() {

    private lateinit var viewModel: HabitViewModel
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var fab: FloatingActionButton

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
        fab = view.findViewById(R.id.fab)

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

        setupBottomSheet(view)

        return view
    }

    private fun setupBottomSheet(view: View) {
        val bottomSheetContainer = view.findViewById<View>(R.id.bottomSheetContainer)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        val bottomSheetFragment = FilterSortBottomSheetFragment.newInstance(bottomSheetBehavior)
        childFragmentManager.beginTransaction()
            .replace(R.id.bottomSheetContainer, bottomSheetFragment)
            .commit()

//        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                viewPager.layoutParams = (viewPager.layoutParams).apply {
//                    height = (viewPager.height - slideOffset).toInt()
//                }
//            }
//        })
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
