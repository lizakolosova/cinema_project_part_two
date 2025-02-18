const searchResults = document.querySelector('#search-results');

document.querySelector('#search').addEventListener('keyup', async e => {
    const searchField = e.target;
    const title = searchField.value;
    const response = await fetch(`/api/movies?title=${title}`, {
        headers: {'Accept': 'application/json'}
    });
    if (response.status === 200) {
        const movies = await response.json();
        if (movies.length > 0)
            searchResults.innerHTML = `<p class="text-muted">Found ${movies.length} movies</p>`
                + '<div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 justify-content-center">';

            searchResults.innerHTML += movies.map(movie => `
                <div class="col mb-3">
                    <div class="card border border-black bg-light text-black rounded col-md-10">
                        <div class="card-body">
                            <h5 class="card-title">${movie.title}</h5>
                            <p class="card-text">Released: ${movie.releaseDate}</p>
                            <p class="card-text">Genre: ${movie.genre}</p>
                            <a href="/movie-details/${movie.id}" class="btn btn-secondary btn-sm btn-light text-dark border border-black">View Details</a>
                        </div>
                    </div>
                </div>
            `).join('');

            searchResults.innerHTML += '</div>';
    } else if (response.status === 204){
        searchResults.innerHTML = '<div class="alert alert-warning">No results found</div>';
    }
});





