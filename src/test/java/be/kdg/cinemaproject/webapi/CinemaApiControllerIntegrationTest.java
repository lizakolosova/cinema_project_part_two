package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.TestHelper;
import be.kdg.cinemaproject.domain.Cinema;
import be.kdg.cinemaproject.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.stream.Stream;

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

    private static String buildTicketJson(String showtime, Integer price, String format, String image, String availability, Long movieId, Long cinemaId
    ) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        if (showtime != null) {
            sb.append(String.format("%s\"showtime\": \"%s\"", "", showtime));
            first = false;
        }
        if (price != null) {
            sb.append(String.format("%s\"price\": %d", first ? "" : ", ", price));
            first = false;
        }
        if (format != null) {
            sb.append(String.format("%s\"format\": \"%s\"", first ? "" : ", ", format));
            first = false;
        }
        if (image != null) {
            sb.append(String.format("%s\"image\": \"%s\"", first ? "" : ", ", image));
            first = false;
        }
        if (availability != null) {
            sb.append(String.format("%s\"availability\": \"%s\"", first ? "" : ", ", availability));
            first = false;
        }
        if (movieId != null) {
            sb.append(String.format("%s\"movieId\": %d", first ? "" : ", ", movieId));
            first = false;
        }
        if (cinemaId != null) {
            sb.append(String.format("%s\"cinemaId\": %d", first ? "" : ", ", cinemaId));
        }
        sb.append("}");
        return sb.toString();
    }


    static Stream<String> invalidTicketPayloads() {
        long movieId = 1L;
        long cinemaId = 1L;
        String time = LocalDateTime.now().plusDays(1).toString();

        return Stream.of(
                buildTicketJson(time, null, "3D", "image.jpg", "AVAILABLE", movieId, cinemaId),
                buildTicketJson(null, 11, "3D", "image.jpg", "AVAILABLE", movieId, cinemaId),
                buildTicketJson(time, 11, null, "image.jpg", "AVAILABLE", movieId, cinemaId),
                buildTicketJson(time, 11, "3D", null, "AVAILABLE", movieId, cinemaId)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidTicketPayloads")
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotAddTicketIfMissingAnyField(String payload) throws Exception {
        // Arrange
        Cinema cinema = testHelper.createCinema();

        final var request = post("/api/cinemas/" + cinema.getId() + "/tickets")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload);

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isBadRequest());
        assertEquals(0, (long) testHelper.findTickets().size());
    }
}