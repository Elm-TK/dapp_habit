package com.example.dz3

import InfoFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView

class MainContainerFragment : Fragment() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var ibMenu: ImageButton
    private lateinit var navigationView: NavigationView
    lateinit var habitViewModel: HabitViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activity_main, container, false)

        drawerLayout = view.findViewById(R.id.navigation_drawer_layout)
        ibMenu = view.findViewById(R.id.ibMenu)

        ibMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        navigationView = view.findViewById(R.id.navigation_drawer)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    replaceFragment(HabitsTabFragment())
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                R.id.nav_info -> {
                    replaceFragment(InfoFragment())
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }

                else -> {
                    Toast.makeText(requireContext(), "Unknown menu item", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HabitsTabFragment())
                .commit()
        }
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val habitDao = HabitDatabase.getInstance(requireContext()).habitDao()
        val repository = HabitRepository(habitDao)
        habitViewModel = ViewModelProvider(this, HabitViewModelFactory(repository)).get(HabitViewModel::class.java)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (childFragmentManager.backStackEntryCount > 0) {
                    childFragmentManager.popBackStack()
                } else {
                    requireActivity().finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }
}
