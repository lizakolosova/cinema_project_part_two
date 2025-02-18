const removeMovieButtons = document.querySelectorAll('.remove-movie-button');
removeMovieButtons.forEach(removeMovieButton => {
    removeMovieButton.addEventListener('click', async e => {
        const movieId = removeMovieButton.getAttribute('data-movie-id');
        const response = await fetch(`/api/movies/${movieId}`, {
            method: 'DELETE'
        });
        if (response.status === 204) {
            document.querySelector(`#movie-${movieId}`).remove();
        } else {
            alert('Something went wrong!');
        }
    });
});
