const form = document.querySelector('#add-ticket-form');
form.addEventListener('submit', async e => {
    e.preventDefault();

    const cinemaId = document.querySelector('#cinema').value;

    const url = `/api/cinemas/${cinemaId}/tickets`;

    const response = await fetch(url, {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        },
        method: 'POST',
        body: JSON.stringify({
            price: parseFloat(document.querySelector('#price').value),
            showtime: document.querySelector('#showtime').value,
            format: document.querySelector('#format').value,
            image: document.querySelector('#image').value,
            availability: document.querySelector('#availability').value.toUpperCase(),
            movieId: document.querySelector('#movie').value,
            cinemaId: cinemaId
        })
    });

    if (response.status === 201) {
        const ticket = await response.json();
        alert(`The ticket got created with ID ${ticket.id}!`);
    } else {
        alert('Something went wrong while creating the ticket');
    }
});

