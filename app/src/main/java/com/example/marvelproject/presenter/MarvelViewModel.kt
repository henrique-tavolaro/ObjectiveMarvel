package com.example.marvelproject.presenter


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.marvelproject.domain.model.Result
import com.example.marvelproject.domain.model.RetrofitResponse
import com.example.marvelproject.domain.repository.MarvelRepository
import com.example.marvelproject.domain.repository.Resource
import com.example.marvelproject.utils.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class MarvelViewModel(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _response = MutableLiveData<Event<Resource<RetrofitResponse?>>>()
    val response: LiveData<Event<Resource<RetrofitResponse?>>> = _response

    private var _numPages = 1

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
        viewModelScope.launch {

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

                it.data?.let { response ->
                    if (response.data.total > PAGE_SIZE) {
                        val rest = response.data.total % PAGE_SIZE
                        _numPages = (response.data.total + rest) / PAGE_SIZE
                        _pageNumberList.postValue((1.._numPages).toList())
                    }
                }
                _response.postValue(Event(it))



            }
        }
    }

    fun resetSearchParams() {
        _page.postValue(1)
        _pageNumberList.postValue(listOf())
        _numPages = 1
    }

    fun nextPage(name: String) {
        viewModelScope.launch {
            if (page.value!! < _numPages) {
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
        }
    }

    fun previousPage(
        name: String
    ) {
        viewModelScope.launch {
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
        }
    }


}
