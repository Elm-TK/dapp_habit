package com.example.presentation

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle.State
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.presentation.R
import com.example.presentation.fragments.CreateOrEditHabitFragment
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@Config(manifest = "src/main/AndroidManifest.xml")
@RunWith(AndroidJUnit4::class)
class CreateOrEditHabitFragmentTest {

    private lateinit var scenario: FragmentScenario<CreateOrEditHabitFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer()
        scenario.moveToState(State.STARTED)
    }

    @Test
    fun testFragmentInitialState() {
        scenario.onFragment { fragment ->
            assertEquals("", fragment.titleInput.text.toString())
            assertEquals("", fragment.descriptionInput.text.toString())
            assertEquals("", fragment.repeatInput.text.toString())
            assertEquals("", fragment.daysInput.text.toString())
            assertEquals(0, fragment.prioritySelector.selectedItemPosition)
        }
    }
}
