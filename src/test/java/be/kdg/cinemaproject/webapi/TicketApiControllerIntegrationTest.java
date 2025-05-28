package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.TestHelper;
import be.kdg.cinemaproject.domain.Ticket;
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
import org.springframework.test.web.servlet.ResultActions;

import static be.kdg.cinemaproject.TestHelper.ADMIN_EMAIL;
import static be.kdg.cinemaproject.TestHelper.VISITOR_EMAIL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TicketApiControllerIntegrationTest {

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
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldDeleteTicketAsVisitor() throws Exception {
        Long ticketId = testHelper.addTicketToVisitor(VISITOR_EMAIL).getId();

        performDeleteTicket(ticketId).andExpect(status().isNoContent());
        assertEquals(0, testHelper.findTickets().size());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldDeleteTicketAsAdmin() throws Exception {
        Long ticketId = testHelper.addTicketToVisitor(VISITOR_EMAIL).getId();

        performDeleteTicket(ticketId).andExpect(status().isNoContent());
        assertEquals(0, testHelper.findTickets().size());
    }

    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotDeleteNotExistingTicket() throws Exception {
        // Arrange
        Long ticketId = 9L;

        // Act
        var response = performDeleteTicket(ticketId);

        // Assert
        response.andExpect(status().isNotFound());
        assertEquals(0, testHelper.findTickets().size());
    }

    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotDeleteTicketAsNotAssignedVisitor() throws Exception {
        // Arrange
        Ticket ticket = testHelper.createTicket();
        Long ticketId = ticket.getId();

        // Act
        var response = performDeleteTicket(ticketId);

        // Assert
        response.andExpect(status().isForbidden());
        assertEquals(1, testHelper.findTickets().size());
    }

    @Test
    void shouldNotDeleteTicketAsAnonymous() throws Exception {
        // Arrange
        Ticket ticket = testHelper.createTicket();
        Long ticketId = ticket.getId();

        // Act
        var response = performDeleteTicket(ticketId);

        // Assert
        response.andExpect(status().isUnauthorized());
        assertEquals(1, (long) testHelper.findTickets().size());
    }

    private ResultActions performDeleteTicket(Long ticketId) throws Exception {
        final var request = delete("/api/tickets/{id}", ticketId)
                .with(csrf());
        return mockMvc.perform(request);
    }

    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldPatchTicketAsVisitor() throws Exception {
        performPatchTicket(VISITOR_EMAIL);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldPatchTicketAsAdmin() throws Exception {
        performPatchTicket(ADMIN_EMAIL);
    }

    private void performPatchTicket(String userEmail) throws Exception {
        Long ticketId = testHelper.addTicketToVisitor(userEmail).getId();

        final var request = patch("/api/tickets/{id}", ticketId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                {
                 "price": 11,
                 "availability": "AVAILABLE"
                 }""");

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.price").value(11))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.availability").value("AVAILABLE"));
    }


    @Test
    @WithUserDetails(value = VISITOR_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotPatchTicketAsNotAssignedVisitor() throws Exception {

        // Arrange
        Long ticketId = testHelper.createTicket().getId();
        final var request =patch("/api/tickets/{id}", ticketId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                {
                 "price": 11,
                 "availability": "AVAILABLE"
                 }""");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldNotPatchTicketAsAnonymous() throws Exception {

        // Arrange
        Long ticketId = testHelper.createTicket().getId();
        final var request =patch("/api/tickets/{id}", ticketId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                {
                 "price": 11,
                 "availability": "AVAILABLE"
                 }""");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithUserDetails(value = ADMIN_EMAIL, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void shouldNotPatchTicketWithTooHighPrice() throws Exception {

        // Arrange
        Long ticketId = testHelper.createTicket().getId();
        final var request =patch("/api/tickets/{id}", ticketId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("""
                {
                 "price": 16,
                 "availability": "AVAILABLE"
                 }""");

        // Act
        final var response = mockMvc.perform(request);

        // Assert
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }
}