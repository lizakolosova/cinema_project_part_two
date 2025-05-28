package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class MovieApiControllerIntegrationTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void tearDown() {
        testHelper.cleanUp();
    }

    @Test
    void shouldSearchMovies() throws Exception {
        testHelper.createMovieWithTitleAndRating("Good title", 9);
        testHelper.createMovieWithTitleAndRating("Bad title", 8);

        final var request = get("/api/movies?title=tItLe")
                .with(csrf());

        final var response = mockMvc.perform(request);

        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Good title"))
                .andExpect(jsonPath("$[1].title").value("Bad title"))
                .andExpect(jsonPath("$", hasSize(2)));
    }
}