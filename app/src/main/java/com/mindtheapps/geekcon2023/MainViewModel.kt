package com.mindtheapps.geekcon2023

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    fun fetchOpenAIResponse(prompt: String) {
        viewModelScope.launch {
            try {
                val result = OpenAIRepository.fetchResponse(prompt)
                _response.value = result
            } catch (e: Exception) {
                _response.value = "Error: ${e.message}"
            }
        }
    }
}
