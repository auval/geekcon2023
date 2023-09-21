package com.mindtheapps.geekcon2023

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> get() = _response

    private val _isApiKeySet = MutableLiveData<Boolean>()
    val isApiKeySet: LiveData<Boolean> get() = _isApiKeySet

    fun fetchOpenAIResponse(prompt: String, context: Context) {
        val apiKey = getAPIKey(context) ?: return // or handle missing API key appropriately

        viewModelScope.launch {
            try {
                val result = OpenAIRepository.fetchResponse(prompt, apiKey)
                _response.value = result
            } catch (e: Exception) {
                _response.value = "Error: ${e.message ?: e.toString()}"
            }
        }
    }

    fun checkAndSetApiKeyStatus(context: Context) {
        _isApiKeySet.value = getAPIKey(context) != null
    }

    fun saveAPIKey(context: Context, apiKey: String) {
        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("openai_api_key", apiKey)
            apply()
        }
        _isApiKeySet.value = true // Update the LiveData after saving the API key
    }

    private fun getAPIKey(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("openai_api_key", null)
    }

}
