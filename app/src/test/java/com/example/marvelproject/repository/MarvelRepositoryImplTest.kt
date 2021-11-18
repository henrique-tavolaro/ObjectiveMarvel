package com.example.marvelproject.repository

import com.example.marvelproject.datasource.FakeWebServerResponse.searchResultResponse
import com.example.marvelproject.datasource.RetrofitService
import com.example.marvelproject.domain.repository.MarvelRepository
import com.example.marvelproject.domain.repository.Status
import com.example.marvelproject.utils.PAGE_SIZE
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class MarvelRepositoryImplTest{


    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val DUMMY_TOKEN = "43234"
    private val DUMMY_QUERY = "Iron Man"
    private val DUMMY_TS = "1234"
    private val DUMMY_HASH = "123123"
    private val OFFSET = 0

    //system in test
    private lateinit var repository: MarvelRepository

    // dependencies
    private lateinit var retrofitService: RetrofitService

    @Before
    fun setup(){
        mockWebServer = MockWebServer()
        mockWebServer.start()
        baseUrl = mockWebServer.url("characters/")
        retrofitService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RetrofitService::class.java)

        repository = MarvelRepositoryImpl(retrofitService)
    }

    @Test
    fun `Get character from network and emit`() = runBlocking {


        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(searchResultResponse)
        )

        val flowItems = repository.searchCharacter(
            DUMMY_QUERY, PAGE_SIZE, OFFSET, DUMMY_TS, DUMMY_TOKEN, DUMMY_HASH)
            .toList()

        //first emission should be loading
        assert(flowItems[0].status == Status.LOADING)

        // second emission should be the response
        val response = flowItems[1].data

        assert(response != null)

        assert(flowItems[1].status == Status.SUCCESS)
        assert(flowItems[1].status != Status.LOADING)


    }


    @Test
    fun `try to get charactor and emit error`() = runBlocking {

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .setBody("{}")
        )

        val flowItems = repository.searchCharacter(DUMMY_QUERY, PAGE_SIZE, OFFSET, DUMMY_TS, DUMMY_TOKEN, DUMMY_HASH).toList()

        //first emission should be loading
        assert(flowItems[0].status == Status.LOADING)

        // second emission should be the error
        val response = flowItems[1].data

        assert(response == null)
        // assert(flowItems[1].status == Status.ERROR)


        //loading should be false
        assert(flowItems[1].status != Status.LOADING)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

}