package com.example.marvelproject.presenter

import android.content.Intent
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.marvelproject.MainActivity
import com.example.marvelproject.R
import com.example.marvelproject.createRule
import com.example.marvelproject.repository.FakeRepositoryImpl
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeFragmentTest {



    private val modules = module {
        val repository = FakeRepositoryImpl()
        viewModel{MarvelViewModel(repository)}
    }



    @Before
    fun setup(){
        loadKoinModules(modules)
        launchFragmentInContainer<HomeFragment>()
    }

    @Test
    fun shouldDisplayRecyclerViewResults() {
//        onView(withId(R.id.tv_nome)).check(matches(isDisplayed()))
    }

}