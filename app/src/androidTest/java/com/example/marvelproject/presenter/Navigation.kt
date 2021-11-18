package com.example.marvelproject.presenter

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.marvelproject.R
import com.example.marvelproject.repository.FakeRepositoryImpl
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

class NavigationTest {

    private val modules = module {
        val repository = FakeRepositoryImpl()
        viewModel { MarvelViewModel(repository) }
    }


    @Test
    fun clickOnFirstItemOfRecyclerView_navigateToDetailsScreen() {


        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext()
        )
        loadKoinModules(modules)
        // Create a graphical FragmentScenario for the TitleScreen
        launchFragmentInContainer() {
            HomeFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created
                        navController.setGraph(R.navigation.main_graph)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }



        Espresso.onView(withId(R.id.et_search))
            .perform(typeText("iron man"))
            .perform(pressImeActionButton())
        Espresso.onView(withId(R.id.rv_home))
            .perform(
                RecyclerViewActions
                    .actionOnItemAtPosition<RecyclerView.ViewHolder>(0, ViewActions.click())
            )

        assert(navController.currentDestination?.id == R.id.detailsFragment)
    }

}