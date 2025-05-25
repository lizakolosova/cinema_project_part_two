import { validateLoginForm } from './login-validation.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.querySelector('form[method="post"]');
    const usernameInput = document.getElementById('username');
    const passwordInput = document.getElementById('password');

    let errorContainer = document.createElement('div');
    errorContainer.className = 'alert alert-danger mt-2 d-none';
    form.insertBefore(errorContainer, form.querySelector('.text-center'));

    form.addEventListener('submit', function (e) {
        errorContainer.innerHTML = '';
        errorContainer.classList.add('d-none');

        const formData = {
            username: usernameInput.value.trim(),
            password: passwordInput.value
        };

        const { error } = validateLoginForm(formData);

        if (error) {
            e.preventDefault();
            error.details.forEach(err => {
                const p = document.createElement('p');
                p.textContent = err.message;
                errorContainer.appendChild(p);
            });
            errorContainer.classList.remove('d-none');
        }
    });
});
