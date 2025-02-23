const form = document.querySelector('#patch-ticket-modal');
form.addEventListener('submit', async e => {
    e.preventDefault();
    const ticketId = form.getAttribute(`data-ticket-id`);

            const response = await fetch(`/api/tickets/${ticketId}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                method: 'PATCH',
                body: JSON.stringify({
                    price: document.querySelector('#edit-price').value,
                    showtime: document.querySelector('#edit-showtime').value,
                    availability: document.querySelector('#edit-availability').value,
                }),
            });

            if (response.status === 201) {
                const ticket = await response.json();
                alert(`The ticket with ID ${ticket.id} got updated!`);
            } else {
                alert('Failed to update ticket');
            }
});

