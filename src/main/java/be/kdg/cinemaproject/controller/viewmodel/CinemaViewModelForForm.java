package be.kdg.cinemaproject.controller.viewmodel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CinemaViewModelForForm {

    @NotBlank(message = "Cinema name is required.")
    private String name;

    @Positive(message = "Capacity must be a positive number.")
    private int capacity;

    @NotBlank(message = "Address is required.")
    private String address;

    @NotBlank(message = "Image name is required.")
    private String image;
}

