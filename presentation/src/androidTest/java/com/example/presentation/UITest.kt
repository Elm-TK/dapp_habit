package com.example.presentation

import junit.framework.TestCase
import org.junit.Rule

class UITest: TestCase() {

    @get:Rule
    var activityTestRule = MainActivity::class.java

    fun testUI(){
        TestCreateAndEditFragment.fill()
    }
}