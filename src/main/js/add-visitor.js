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
    if (!response.ok) {
        const errorText = await response.text();
        console.error('Error response:', errorText);
        alert(`Failed to create user: ${errorText}`);
        return;
    }
    if (response.status === 201) {
        const data = await response.json();
        console.log('User created successfully:', data);
        alert('User created successfully!');
        window.location.href = "/login";
    } else {
        const contentType = response.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
            const errorData = await response.json();
            console.error('Failed to create user:', errorData);
            alert(`Failed to create user: ${errorData.message || 'Unknown error'}`);
        } else {
            const errorText = await response.text();
            console.error('Unexpected error response:', errorText);
            alert(`Failed to create user. Server response was not JSON:\n${errorText}`);
        }
    }
});