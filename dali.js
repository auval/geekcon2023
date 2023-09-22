async function generateImage() {
    const endpoint = "https://api.openai.com/v1/images/generations";
    const apiKey = document.getElementById('apiKey').value;
    const promptText = document.getElementById('prompt').value;

    const requestBody = {
        prompt: promptText,
        n: 1,
        size: "1024x1024"
    };

    try {
        const response = await fetch(endpoint, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${apiKey}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestBody)
        });

        const data = await response.json();

        // Handle the response data as needed
        console.log(data);

        // If the response contains a direct link to the generated image:
        const imageUrl = data.data[0].url; // Adjust this based on the actual response structure

        // Display the image in your HTML (if desired)
        const imgElement = document.createElement("img");
        imgElement.src = imageUrl;
        document.body.appendChild(imgElement);

        return imageUrl;
    } catch (error) {
        console.error("Error generating image:", error);
    }
}


// Retrieve the API key from the cookie when the page loads
window.addEventListener('load', function () {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const [name, value] = cookie.trim().split('=');
        if (name === 'apiKey') {
            // Set the API key input field to the stored value
            document.getElementById('apiKey').value = value;
            break;
        }
    }
});