const form = document.querySelector('#add-visitor-form');
form.addEventListener('submit', async e => {
    e.preventDefault();

    const response = await fetch(`/api/visitors`, {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify({
            name: document.querySelector('#name').value,
            email: document.querySelector('#email').value,
            password: document.querySelector('#password').value,
            role: document.querySelector('#role').value
        }),
    });
    if (!response.ok) {  // Check if the response is not OK (status code 2xx)
        const errorText = await response.text(); // Get the raw error message (likely HTML)
        console.error('Error response:', errorText);
        alert(`Failed to create user: ${errorText}`);
        return;  // Exit early to prevent parsing JSON when the response is an error
    }
    if (response.status === 201) {
        const data = await response.json();
        console.log('User created successfully:', data);
        alert('User created successfully!');
        form.reset();
    } else {
        const errorData = await response.json();
        console.error('Failed to create user:', errorData);
        alert(`Failed to create user: ${errorData.message || 'Unknown error'}`);
    }
});