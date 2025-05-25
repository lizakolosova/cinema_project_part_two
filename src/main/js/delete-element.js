import {csrfToken, csrfHeaderName} from "./util/csrf.js";
import Swal from 'sweetalert2';
import {animate} from "animejs";

document.addEventListener('DOMContentLoaded', () => {
    setupDeleteButtons('cinema');
    setupDeleteButtons('movie');
    setupDeleteButtons('ticket');
});

function setupDeleteButtons(type) {
    const removeButtons = document.querySelectorAll(`.remove-${type}-button`);

    removeButtons.forEach(button => {
        button.addEventListener('click', async () => {
            const itemId = button.getAttribute(`data-${type}-id`);

            const result = await Swal.fire({
                title: 'Are you sure?',
                text: `Do you really want to delete this ${type}? This action cannot be undone.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#9fbeff',
                cancelButtonColor: '#ff9b9b',
                confirmButtonText: 'Yes, delete it!',
                cancelButtonText: 'Cancel'
            });
            console.log(document.querySelector(`#${type}-${itemId}`));
            console.log(document.querySelector(`#${type}-${itemId} .card`));


            if (!result.isConfirmed) {
                return;
            }

            button.disabled = true;
            const response = await fetch(`/api/${type}s/${itemId}`,
                {
                    method: 'DELETE',
                    headers: {
                        [csrfHeaderName]: csrfToken
                    }
                }
            );
            button.disabled = false;
            if (response.status === 204) {
                const card = button.closest('.card');
                const col = card.closest('.col');

                animate(
                    card,
                    {
                        opacity: [1, 0],
                        translateY: [0, -24],
                        scale: [1, 0.94],
                        duration: 800,
                        easing: 'easeInOutQuad',
                        onComplete: function() {
                            col.remove();
                            Swal.fire('Deleted!', `The ${type} was successfully deleted!`, 'success');
                        }
                    }
                );
            } else {
                await Swal.fire('Error', 'Failed to delete', 'error');
            }
        });
    });
}
