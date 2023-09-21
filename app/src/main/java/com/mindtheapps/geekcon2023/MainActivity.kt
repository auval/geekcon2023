package com.mindtheapps.geekcon2023

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mindtheapps.geekcon2023.ui.theme.Geekcon2023Theme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.checkAndSetApiKeyStatus(this@MainActivity) // Check API key status on app start

        setContent {
            Geekcon2023Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    OpenAIUI(viewModel)
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun OpenAIUI(viewModel: MainViewModel) {

        val response by viewModel.response.observeAsState(initial = "")
        var prompt by remember { mutableStateOf("what is the wave length of visible light?") }
        var showDialog by remember { mutableStateOf(false) }
        var apiKeyInput by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text("Prompt") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            val isApiKeySet by viewModel.isApiKeySet.observeAsState(initial = false)

            Button(
                onClick = { viewModel.fetchOpenAIResponse(prompt, baseContext) },
                enabled = isApiKeySet
            ) {
                Text("Fetch Response")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = response,
                onValueChange = { prompt = it },
                readOnly = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                showDialog = true
                viewModel.checkAndSetApiKeyStatus(baseContext) // Check if API key is set after showing the dialog
            }) {
                Text("Set OpenAI API Key")
            }

        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Enter OpenAI API Key") },
                text = {
                    TextField(
                        value = apiKeyInput,
                        onValueChange = { apiKeyInput = it },
                        label = { Text("API Key") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        viewModel.saveAPIKey(baseContext, apiKeyInput)
                        showDialog = false
                    }) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )

        }

    }

    @Preview(showBackground = true)
    @Composable
    fun OpenAIUIPreview() {
        Geekcon2023Theme {
            OpenAIUI(MainViewModel())
        }
    }
}
