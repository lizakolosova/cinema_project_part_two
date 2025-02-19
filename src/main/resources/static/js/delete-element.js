document.addEventListener('DOMContentLoaded', () => {
    setupDeleteButtons('cinema');
    setupDeleteButtons('movie');
});

function setupDeleteButtons(type) {
    const removeButtons = document.querySelectorAll(`.remove-${type}-button`);

    removeButtons.forEach(button => {
        button.addEventListener('click', async () => {
            const itemId = button.getAttribute(`data-${type}-id`);
            const response = await fetch(`/api/${type}s/${itemId}`, {
                method: 'DELETE'
            });

            if (response.status === 204) {
                document.querySelector(`#${type}-${itemId}`)?.remove();
            } else {
                alert('Something went wrong!');
            }
        });
    });
}
