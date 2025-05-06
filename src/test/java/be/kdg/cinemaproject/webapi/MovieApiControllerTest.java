package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.TestHelper;
import be.kdg.cinemaproject.controller.viewmodel.MoviesViewModel;
import be.kdg.cinemaproject.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static be.kdg.cinemaproject.TestHelper.VISITOR_EMAIL;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class MovieApiControllerTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void tearDown() {
        testHelper.cleanUp();
        testHelper.createAdmin();
        testHelper.createVisitorWithTicket();
    }

    @Test
    void shouldSearchMovies() throws Exception {
        testHelper.createMovieWithTitleAndRating("Good title", 9);
        testHelper.createMovieWithTitleAndRating("Bad title", 8);

        final var request = get("/api/movies?title=title") // fixed param
                .with(csrf());

        final var response = mockMvc.perform(request);

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Good title"))
                .andExpect(jsonPath("$[1].title").value("Bad title"))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}