async function generateImage() {
    const endpoint = "https://api.openai.com/v1/engines/dali/completions"; // This is a hypothetical endpoint; the actual one might differ.
    const apiKey = document.getElementById('apiKey').value;
    const promptText = document.getElementById('prompt').value;
    // const prefix = document.getElementById('prefix').value; // This line is unused in the current function.

    const requestBody = {
        prompt: promptText,
        // Add any other necessary parameters here
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

        if (!response.ok) {
            throw new Error(`API call failed with status ${response.status}`);
        }

        const data = await response.json();

        // Assuming the response contains a direct link to the generated image
        const imageUrl = data.image_url; // This is hypothetical; the actual key might differ.

        const imgElement = document.createElement("img");
        imgElement.src = imageUrl;
        document.body.appendChild(imgElement);

        return imageUrl;
    } catch (error) {
        console.error("Error generating image:", error);
        alert("Error generating image: " + error.message);
    }
}

// Add an event listener to the button
//document.getElementById('generateImageButton').addEventListener('click', generateImage);


/*
// Usage example:
generateImage("A sunset over a mountain range", "YOUR_OPENAI_API_KEY")
    .then(imageUrl => {
        // Display the image in your HTML
        const imgElement = document.createElement("img");
        imgElement.src = imageUrl;
        document.body.appendChild(imgElement);
    })
    .catch(error => {
        console.error("Error generating image:", error);
    });
*/

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