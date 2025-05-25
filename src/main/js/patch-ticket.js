import Swal from 'sweetalert2';
import {csrfToken, csrfHeaderName} from "./util/csrf.js";

const form = document.querySelector('#patch-ticket-modal');
form.addEventListener('submit', async e => {
    e.preventDefault();

    Swal.fire({
        title: 'Are you sure?',
        text: "Do you want to save these changes?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#9fbeff',
        cancelButtonColor: '#ff9b9b',
        confirmButtonText: 'Yes, save it!',
        cancelButtonText: 'Cancel'
    }).then(async (result) => {
        if (result.isConfirmed) {
            const ticketId = form.getAttribute('data-ticket-id');
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
                Swal.fire('Updated!', `The ticket with ID ${ticketId} was successfully updated!`, 'success')
                    .then(() => window.location.reload());
            } else {
                await Swal.fire('Error', 'Failed to update ticket', 'error');
            }
        }
    });
});
