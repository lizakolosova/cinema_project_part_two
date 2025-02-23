package be.kdg.cinemaproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataAccessException.class)
    public String handleDatabaseException(DataAccessException ex, Model model) {
        log.error("Database error occurred: {}", ex.getMessage());
        model.addAttribute("status", 500);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("timestamp", LocalDateTime.now());
        return "error/database-error";
    }
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public Object handleNotFoundException(final HttpServletRequest request, final ChangeSetPersister.NotFoundException e) {
        if (request.getRequestURI().startsWith("/api")) {
            return new ResponseEntity<>(new ErrorDto(e.getMessage()), HttpStatus.NOT_FOUND);
        }
        ModelAndView modelAndView = new ModelAndView("error/other-error");
        modelAndView.addObject("message", e.getMessage());
        return modelAndView;
    }
    private record ErrorDto(String message) {
    }
}
