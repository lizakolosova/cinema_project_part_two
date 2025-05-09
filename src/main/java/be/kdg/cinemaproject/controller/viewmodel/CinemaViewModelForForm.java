package be.kdg.cinemaproject.controller.viewmodel;
import be.kdg.cinemaproject.domain.Cinema;
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

    public Cinema toCinema() {
        Cinema cinema = new Cinema();
        cinema.setName(this.name);
        cinema.setCapacity(this.capacity);
        cinema.setAddress(this.address);
        cinema.setImage(this.image);
        return cinema;
    }
}

