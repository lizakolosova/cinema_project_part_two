import {csrfToken, csrfHeaderName} from "./util/csrf.js";

const form = document.querySelector('#patch-ticket-modal');
form.addEventListener('submit', async e => {
    e.preventDefault();
    const ticketId = form.getAttribute(`data-ticket-id`);

            const response = await fetch(`/api/tickets/${ticketId}`, {
                method: "PATCH",
                headers: {
                    "Content-Type": 'application/json',
                    [csrfHeaderName]: csrfToken
                },
                body: JSON.stringify({
                    price: document.querySelector('#edit-price').value,
                    showtime: document.querySelector('#edit-showtime').value,
                    availability: document.querySelector('#edit-availability').value,
                }),
            });

            if (response.status === 204) {
                alert(`The ticket with ID ${ticketId} was successfully updated!`);
                window.location.reload();
            } else {
                alert('Failed to update ticket');
            }
});