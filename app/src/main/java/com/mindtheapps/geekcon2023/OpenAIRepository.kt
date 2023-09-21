package com.mindtheapps.geekcon2023

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException
import org.json.JSONObject

object OpenAIRepository {
    private val client = OkHttpClient()

    suspend fun fetchResponse(prompt: String, apiKey: String): String =
        withContext(Dispatchers.IO) {
            val jsonMediaType = "application/json; charset=utf-8".toMediaType()
            val body = """
            {
                "prompt": "$prompt",
                "max_tokens": 150
            }
        """.trimIndent().toRequestBody(jsonMediaType)

            val request = Request.Builder()
                .url("https://api.openai.com/v1/engines/curie/completions")
//                .url("https://api.openai.com/v1/engines/davinci/completions")
                .post(body)
                .addHeader("Authorization", "Bearer $apiKey")
                .build()

            println("@@@ Prompt: [$prompt]")
            println("@@@ Request Body: [$body]")
            try {
                client.newCall(request).execute().use { response ->

                    val responseBody = response.body?.string()
                    println("@@@ Response: $responseBody")
                    println("@@@ Request Body: $body")


                    if (response.code == 429) {
                        return@withContext ("Error 429: Rate limit exceeded.")
                    }

                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    if (responseBody == null) return@withContext "Empty response body"
                    val jsonResponse = JSONObject(responseBody)
                    val text =
                        jsonResponse.getJSONArray("choices").getJSONObject(0).getString("text")
                    return@withContext text
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                throw e
            }

        }
}
