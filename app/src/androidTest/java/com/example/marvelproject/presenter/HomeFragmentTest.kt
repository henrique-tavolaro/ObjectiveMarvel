package com.example.marvelproject.presenter

import android.view.View
import android.widget.TextView
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.pressImeActionButton
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.marvelproject.R
import com.example.marvelproject.repository.FakeRepositoryImpl
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.loadKoinModules
import org.koin.dsl.module

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
        onView(withId(R.id.et_search))
            .perform(typeText("iron man"))
            .perform(pressImeActionButton())
        onView(withId(R.id.rv_home))
            .check(matches(
                showCharacters(
                    0,"Iron Man"
                )
            ))
        onView(withId(R.id.rv_home))
            .check(matches(
                showCharacters(
                    1,"Iron Man (Iron Man 3 - The Official Game)"
                )
            ))
    }



}

fun showCharacters(position: Int, character: String): Matcher<in View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java){
        override fun describeTo(description: Description?) {
            description!!
                .appendText("View com personagem")
                .appendValue(character)
                .appendText("na posição")
                .appendValue(position)
                .appendText("não foi encontrada")
        }

        override fun matchesSafely(item: RecyclerView?): Boolean {
            val viewHolder = item!!.findViewHolderForAdapterPosition(position)!!.itemView
            val cityText = viewHolder.findViewById<TextView>(R.id.tv_home_adapter)
            val expected : Boolean = cityText.text.toString() == character
            return expected
        }

    }
}