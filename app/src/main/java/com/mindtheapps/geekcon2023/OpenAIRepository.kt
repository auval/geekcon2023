package com.mindtheapps.geekcon2023

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

object OpenAIRepository {
    private val client = OkHttpClient()

    suspend fun fetchResponse(prompt: String): String {
        val jsonMediaType = "application/json; charset=utf-8".toMediaType()
        val body = """
            {
                "prompt": "$prompt",
                "max_tokens": 150
            }
        """.trimIndent().toRequestBody(jsonMediaType)

        val request = Request.Builder()
            .url("https://api.openai.com/v1/engines/davinci/completions")
            .post(body)
            .addHeader("Authorization", "Bearer YOUR_OPENAI_API_KEY")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            return response.body!!.string()
        }
    }
}
