package com.example.dz3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dz3.Fragments.MainContainerFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, MainContainerFragment())
                .commit()
        }
    }
}
