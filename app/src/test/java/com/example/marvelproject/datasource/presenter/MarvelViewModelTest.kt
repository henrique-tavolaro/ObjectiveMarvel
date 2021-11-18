package com.example.marvelproject.datasource.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.marvelproject.MainCoroutineRule
import com.example.marvelproject.datasource.FakeRepository
import com.example.marvelproject.datasource.getOrAwaitValueTest
import com.example.marvelproject.domain.repository.MarvelRepository
import com.example.marvelproject.domain.repository.Status
import com.example.marvelproject.presenter.MarvelViewModel
import com.example.marvelproject.utils.PRIVATE_KEY
import com.example.marvelproject.utils.PUBLIC_KEY
import com.example.marvelproject.utils.TS
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigInteger
import java.security.MessageDigest

class MarvelViewModelTest {

    val ts = TS
    val md = MessageDigest.getInstance("MD5")
    val input = ts + PRIVATE_KEY + PUBLIC_KEY
    val hash = BigInteger(1, md.digest(input.toByteArray())).toString(16)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: MarvelViewModel

    private lateinit var repository: MarvelRepository

    @Before()
    fun setup(){
        repository = FakeRepository()
        viewModel = MarvelViewModel(repository)
    }

    @Test
    fun `get list of result from repository`() = runBlocking {

        viewModel.searchCharacter("iron man")

        val value = viewModel.response.getOrAwaitValueTest()

        assert(value.getContentIfNotHandled()?.status == Status.SUCCESS)
    }

    @Test
    fun `assert page number is 2`() = runBlocking {

        viewModel.searchCharacter("iron man")

        val value = viewModel.pageNumberList.getOrAwaitValueTest()

        assert(value.size == 2)

    }

}