import {csrfToken, csrfHeaderName} from "./util/csrf.js";

document.addEventListener('DOMContentLoaded', () => {
    setupDeleteButtons('cinema');
    setupDeleteButtons('movie');
});

function setupDeleteButtons(type) {
    const removeButtons = document.querySelectorAll(`.remove-${type}-button`);

    removeButtons.forEach(button => {
        button.addEventListener('click', async () => {
            const itemId = button.getAttribute(`data-${type}-id`);
            button.disabled = true;
            const response = await fetch(`/api/${type}s/${itemId}`,
                {
                    method: 'DELETE',
                    headers: {
                        [csrfHeaderName]: csrfToken
                    }
                    },
                );
            button.disabled = false;
            if (response.status === 204) {
                document.querySelector(`#${type}-${itemId}`)?.remove();
            } else {
                alert('Something went wrong!');
            }
        });
    });
}
