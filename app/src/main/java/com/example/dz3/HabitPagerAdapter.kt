package com.example.dz3

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class HabitPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ListHabitFragment.newInstance(1, "good")
            1 -> ListHabitFragment.newInstance(1, "bad")
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
