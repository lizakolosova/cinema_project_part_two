package be.kdg.cinemaproject.controller;

import be.kdg.cinemaproject.TestHelper;
import be.kdg.cinemaproject.controller.viewmodel.MoviesViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static be.kdg.cinemaproject.TestHelper.ADMIN_EMAIL;
import static be.kdg.cinemaproject.TestHelper.VISITOR_EMAIL;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovieControllerIntegrationTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void cleanUp() {
        testHelper.cleanUp();
        testHelper.createAdmin();
        testHelper.createVisitor();
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldAddMovie() throws Exception {
        // Arrange
        final var request = post("/movies/addmovie")
                .with(csrf())
                .param("title", "Movie")
                .param("rating", "8")
                .param("genre", "ACTION")
                .param("releaseDate", "2025-11-02")
                .param("image", "image.jpg");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andExpect(status().isFound())
                .andExpect(view().name("redirect:/movies"));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAddMovieWithoutTitle() throws Exception {
        // Arrange
        final var request = post("/movies/addmovie")
                .with(csrf())
                .param("rating", "8")
                .param("genre", "ACTION")
                .param("releaseDate", "2025-11-02")
                .param("image", "image.jpg");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andExpect(status().isOk())
                .andExpect(view().name("movie/addmovie"));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAddMovieWithInvalidRating() throws Exception {
        // Arrange
        final var request = post("/movies/addmovie")
                .with(csrf())
                .param("title", "Movie")
                .param("rating", "15")
                .param("genre", "ACTION")
                .param("releaseDate", "2025-11-02")
                .param("image", "image.jpg");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andExpect(status().isOk())
                .andExpect(view().name("movie/addmovie"));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAddMovieWithInvalidGenre() throws Exception {
        // Arrange
        final var request = post("/movies/addmovie")
                .with(csrf())
                .param("title", "Movie")
                .param("rating", "8")
                .param("genre", "gibberish")
                .param("releaseDate", "2024-11-02")
                .param("image", "image.jpg");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andExpect(status().isOk())
                .andExpect(view().name("movie/addmovie"));
    }


    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAddMovieAsVisitor() throws Exception {
        // Arrange
        final var request = post("/movies/addmovie")
                .with(csrf())
                .param("title", "Movie")
                .param("rating", "8")
                .param("genre", "ACTION")
                .param("releaseDate", "2025-11-02")
                .param("image", "image.jpg");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldFilterMovies() throws Exception {
        // Arrange
        testHelper.createMovieWithTitleAndRating("Good title", 9);
        testHelper.createMovieWithTitleAndRating("Bad title",8);

        final var request = post("/movies/filter")
                .with(csrf())
                .param("title", "title")
                .param("rating", "9");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        final var model = Objects.requireNonNull(response.andDo(print())
                        .andExpect(view().name("movie/movies"))
                        .andExpect(model().attribute("movies", instanceOf(MoviesViewModel.class)))
                        .andReturn()
                        .getModelAndView())
                .getModel();
        final var movies = (MoviesViewModel) model.get("movies");
        assertEquals(1, movies.movies().size());
    }
}