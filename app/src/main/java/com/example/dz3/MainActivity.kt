package com.example.dz3

import InfoFragment
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.widget.Toolbar
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var ibMenu: ImageButton
    private lateinit var navigationView: NavigationView
    lateinit var habitViewModel: HabitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.navigation_drawer_layout)
        ibMenu = findViewById(R.id.ibMenu)

        ibMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        navigationView = findViewById(R.id.navigation_drawer)
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
                    Toast.makeText(this, "Неизвестный пункт меню", Toast.LENGTH_SHORT).show()
                    false
                }
            }
        }

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.frame_layout, com.example.dz3.ListHabitFragment())
//                .commit()
//        }
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, HabitsTabFragment())
                .commit()
        }

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .addToBackStack(null)
            .commit()
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
            } else {
                finish()
            }
        }
    }
}
