package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.TestHelper;
import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import static be.kdg.cinemaproject.TestHelper.VISITOR_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class CinemaApiControllerIntegrationTest {

    @Autowired
    private TestHelper testHelper;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void tearDown() {
        testHelper.cleanUp();
        testHelper.createAdmin();
        testHelper.createVisitor();
    }

    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void ShouldAddTicket() throws Exception {

        //Arrange
        Movie movie = testHelper.createMovie();
        Cinema cinema = testHelper.createCinema();
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        final var request = post("/api/cinemas/" + cinema.getId() + "/tickets")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.format("""
                {
                 "price": 11,
                 "showtime": "%s",
                 "format": "3D",
                 "image": "image.jpg",
                 "availability": "AVAILABLE",
                 "movieId": %d,
                 "cinemaId": %d
                 }""", time, movie.getId(), cinema.getId()));

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(11))
                .andExpect(jsonPath("$.format").value("3D"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.availability").value("AVAILABLE"))
                .andReturn()
                .getResponse()
                .getContentAsString();
        assertEquals(1, (long) testHelper.findTickets().size());
    }

    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void ShouldNotAddTicketWithoutPrice() throws Exception {

        //Arrange
        Movie movie = testHelper.createMovie();
        Cinema cinema = testHelper.createCinema();
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        final var request = post("/api/cinemas/" + cinema.getId() + "/tickets")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(String.format("""
                {
                 "showtime": "%s",
                 "format": "3D",
                 "image": "image.jpg",
                 "availability": "AVAILABLE",
                 "movieId": %d,
                 "cinemaId": %d
                 }""", time, movie.getId(), cinema.getId()));

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isBadRequest());
        assertEquals(0, (long) testHelper.findTickets().size());
    }
}