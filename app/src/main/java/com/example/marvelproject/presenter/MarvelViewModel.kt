package com.example.marvelproject.presenter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelproject.domain.model.Result
import com.example.marvelproject.domain.model.RetrofitResponse
import com.example.marvelproject.domain.repository.MarvelRepository
import com.example.marvelproject.domain.repository.Resource
import com.example.marvelproject.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class MarvelViewModel(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>(false)
    val loading : LiveData<Boolean> = _loading

    private val _response = MutableLiveData<Event<Resource<RetrofitResponse>>>()
    val response: LiveData<Event<Resource<RetrofitResponse>>> = _response

    private val _numPages = MutableLiveData<Int>(1)
    val numPages: LiveData<Int> = _numPages

    private val _page = MutableLiveData<Int>(1)
    val page: LiveData<Int> = _page

    private val _pageNumberList = MutableLiveData<List<Int>>(listOf())
    val pageNumberList: LiveData<List<Int>> = _pageNumberList

    private val _navigateToDetails = MutableLiveData<Result?>()
    val navigateToDetails get() = _navigateToDetails

    fun onCharacterClicked(result: Result) {
        _navigateToDetails.value = result
    }

    fun onDetailsNavigated() {
        _navigateToDetails.value = null
    }

    fun searchCharacter(
        name: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _loading.postValue(true)
            val ts = TS
            val md = MessageDigest.getInstance("MD5")
            val input = ts + PRIVATE_KEY + PUBLIC_KEY
            val hash = BigInteger(1, md.digest(input.toByteArray())).toString(16)
            val offset = if (_page.value == 1) 0 else PAGE_SIZE * _page.value!!

            repository.searchCharacter(
                name = name,
                limit = PAGE_SIZE,
                offset = offset,
                ts = ts,
                apikey = PUBLIC_KEY,
                hash = hash
            ).collect {
                if (it.data!!.data.total > PAGE_SIZE) {
                    val rest = it.data.data.total % PAGE_SIZE
                    _numPages.postValue((it.data.data.total + rest) / PAGE_SIZE)
                    _pageNumberList.postValue((1..numPages.value!!).toList())
                }
                _response.postValue(Event(it))


            }
            _loading.postValue(false)
        }
    }

    fun resetSearchParams() {
        _page.postValue(1)
        _pageNumberList.postValue(listOf())
    }

    fun nextPage(name: String) {
        viewModelScope.launch{
            _loading.postValue(true)
            if (page.value!! < numPages.value!!) {
                _page.postValue(page.value!! + 1)

                val ts = TS
                val md = MessageDigest.getInstance("MD5")
                val input = ts + PRIVATE_KEY + PUBLIC_KEY
                val hash = BigInteger(1, md.digest(input.toByteArray())).toString(16)

                val offset = if (page.value == 1) 0 else PAGE_SIZE * (page.value!! - 1)

                repository.searchCharacter(
                    name = name,
                    limit = PAGE_SIZE,
                    offset = offset,
                    ts = ts,
                    apikey = PUBLIC_KEY,
                    hash = hash
                ).collect {
                    _response.postValue(Event(it))
                    }
            }
            _loading.postValue(false)
        }
    }

    fun previousPage(
        name: String
    ) {
        viewModelScope.launch {
            _loading.postValue(true)
            if (page.value!! > 1) {
                _page.postValue(page.value!! - 1)

                val ts = TS
                val md = MessageDigest.getInstance("MD5")
                val input = ts + PRIVATE_KEY + PUBLIC_KEY
                val hash = BigInteger(1, md.digest(input.toByteArray())).toString(16)

                val offset = if (page.value == 1) 0 else PAGE_SIZE * (page.value!! - 1)

                repository.searchCharacter(
                    name = name,
                    limit = PAGE_SIZE,
                    offset = offset,
                    ts = ts,
                    apikey = PUBLIC_KEY,
                    hash = hash
                ).collect {
                    _response.postValue(Event(it))
                }
            }
            _loading.postValue(false)
        }
    }


    }
