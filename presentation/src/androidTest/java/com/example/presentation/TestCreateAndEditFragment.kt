package com.example.presentation

import com.example.presentation.fragments.CreateOrEditHabitFragment
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton

object TestCreateAndEditFragment : KScreen<TestCreateAndEditFragment>() {
    private val titleInput = KEditText { withId(R.id.title_input) }
    private val descriptionInput = KEditText { withId(R.id.description_input) }


    fun fill() {
        titleInput.typeText("Habit 1")
        descriptionInput.typeText("desc")
    }

    override val layoutId: Int? = R.layout.fragment_create_or_edit_habit
    override val viewClass: Class<*> = CreateOrEditHabitFragment::class.java
}