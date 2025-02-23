const form = document.querySelector('#add-ticket-form');
form.addEventListener('submit', async e => {
    e.preventDefault();

    const response = await fetch('/api/tickets', {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify({
            price: document.querySelector('#price').value,
            showTime: document.querySelector('#showtime').value,
            format: document.querySelector('#format').value,
            image: document.querySelector('#image').value,
            availability: document.querySelector('#availability').value,
        })
    });

    if (response.status === 201) {
        const ticket = await response.json();
        alert(`The ticket got created with ID ${ticket.id}!`);
    } else {
        alert('Something went wrong while creating the ticket');
    }
});
