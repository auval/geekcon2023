// script.js
function fetchResponse() {
    const apiKey = document.getElementById('apiKey').value;
    const prompt = document.getElementById('prompt').value;

 //   const url = "https://api.openai.com/v1/engines/davinci/completions";
    const url = "https://api.openai.com/v1/chat/completions";
    
    const messages = [
        {"role": "system", "content": "You are a helpful assistant."},
        {"role": "user", "content": prompt}
    ];
    const body = {
        model: "gpt-3.5-turbo",
        messages: messages,
        prompt: prompt,
        max_tokens: 150
    };

    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${apiKey}`,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(body)
    })
    .then(response => response.json())
    .then(data => {
        console.log("response"  + response);
        console.log("data.choices"  + data.choices);
        const text = data.choices && data.choices[0] && data.choices[0].text;
        document.getElementById('response').innerText = text;
    })
    .catch(error => {
        console.error("Error:", error);
        document.getElementById('response').innerText = "Error: " + error.message;
    });
}
