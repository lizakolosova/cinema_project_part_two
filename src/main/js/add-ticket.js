import { validateAddTicketForm } from './add-ticket-validation.js';
import { csrfToken, csrfHeaderName } from "./util/csrf.js";
import { doConfetti } from './confetti.js';
import Swal from 'sweetalert2';

const form = document.querySelector('#add-ticket-form');
const errorMessage = document.getElementById('errorMessage');

form.addEventListener('submit', async e => {
    e.preventDefault();

    const formData = {
        price: document.querySelector('#price').value,
        showtime: document.querySelector('#showtime').value,
        format: document.querySelector('#format').value,
        image: document.querySelector('#image').value,
        availability: document.querySelector('#availability').value,
        movie_id: document.querySelector('#movie').value,
        cinema_id: document.querySelector('#cinema').value
    };

    const { error } = validateAddTicketForm(formData);

    errorMessage.innerHTML = '';
    errorMessage.classList.add('d-none');
    if (error) {
        error.details.forEach(err => {
            const p = document.createElement('p');
            p.textContent = err.message;
            errorMessage.appendChild(p);
        });
        errorMessage.classList.remove('d-none');
        return;
    }

    const url = `/api/cinemas/${formData.cinema_id}/tickets`;

    const response = await fetch(url, {
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            [csrfHeaderName]: csrfToken
        },
        method: 'POST',
        body: JSON.stringify({
            price: parseFloat(formData.price),
            showtime: formData.showtime,
            format: formData.format,
            image: formData.image,
            availability: formData.availability.toUpperCase(),
            movieId: formData.movie_id,
            cinemaId: formData.cinema_id
        })
    });

    if (response.status === 201) {
        const ticket = await response.json();
        doConfetti();
        await Swal.fire({
            title: 'Success!',
            text: `The ticket was created with ID ${ticket.id}`,
            icon: 'success',
            confirmButtonColor: '#598bf8'
        });
        form.reset();
    } else {
        await Swal.fire({
            title: 'Oops!',
            text: 'Something went wrong while creating the ticket.',
            icon: 'error',
            confirmButtonColor: '#598bf8'
        });
    }
});
