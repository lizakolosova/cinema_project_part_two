package be.kdg.cinemaproject.webapi;

import be.kdg.cinemaproject.domain.Visitor;
import be.kdg.cinemaproject.service.VisitorService;
import be.kdg.cinemaproject.webapi.dto.visitor.AddVisitorDto;
import be.kdg.cinemaproject.webapi.dto.visitor.VisitorDto;
import be.kdg.cinemaproject.webapi.dto.visitor.VisitorMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/visitors")
public class VisitorApiController {
    private final VisitorMapper visitorMapper;

    private final VisitorService visitorService;

    public VisitorApiController(VisitorMapper visitorMapper, VisitorService visitorService) {
        this.visitorMapper = visitorMapper;
        this.visitorService = visitorService;
    }

    @PostMapping()
    public ResponseEntity<VisitorDto> add(@RequestBody @Valid final AddVisitorDto addVisitorDto) {
        final Visitor visitor = visitorService.add(addVisitorDto.name(), addVisitorDto.email(), addVisitorDto.password(), addVisitorDto.role());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(visitorMapper.toVisitorDto(visitor));
    }
}
