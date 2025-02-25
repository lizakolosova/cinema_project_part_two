const searchResults = document.querySelector('#search-results');

document.querySelector('#search').addEventListener('keyup', async e => {
    const searchField = e.target;
    const title = searchField.value;
    const response = await fetch(`/api/movies?title=${title}`, {
        headers: {'Accept': 'application/json'}
    });
    if (response.status === 200) {
        const movies = await response.json();
        if (movies.length > 0) {
            searchResults.innerHTML = `
        <p class="text-muted text-center">Found ${movies.length} movies</p>
        <div class="row row-cols-1 row-cols-sm-2 row-cols-md-1 g-3 mx-auto" style="max-width: 900px;">
            ${movies.map(movie => `
                <div class="col mb-3">
                    <div class="card border border-warning bg-warning-subtle text-black rounded">
                        <div class="card-body">
                            <h5 class="card-title">${movie.title}</h5>
                            <p class="card-text">Released: ${movie.releaseDate}</p>
                            <p class="card-text">Genre: ${movie.genre}</p>
                            <a href="/movies/details/${movie.id}" class="btn btn-secondary btn-sm btn-warning text-dark border border-warning">View Details</a>
                        </div>
                    </div>
                </div>
            `).join('')}
        </div>`;
        }
    } else if (response.status === 204){
        searchResults.innerHTML = '<div class="alert alert-warning">No results found</div>';
    }
});





