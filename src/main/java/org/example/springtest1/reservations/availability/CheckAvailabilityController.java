package org.example.springtest1.reservations.availability;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("reservation/v1/availability")
public class CheckAvailabilityController {
    private final Logger log = LoggerFactory.getLogger(CheckAvailabilityController.class);

    private final CheckAvailabilityService service;



    public CheckAvailabilityController(CheckAvailabilityService service) {
        this.service = service;
    }


    @PostMapping("/check")
    public ResponseEntity<CheckAvailabilityResponse> checkAvailability(
            @Valid @RequestBody CheckAvailabilityRequest request
    ){
        var isAvailable = service.isReservationAvailable(
                request.roomId(),
                request.startDate(),
                request.endDate()
        );

        var message = isAvailable
                ? "Reservation Available"
                : "Reservation Unavailable";
        var status = isAvailable
                ? CheckAvailabilityStatus.AVAILABLE
                : CheckAvailabilityStatus.RESERVED;

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new CheckAvailabilityResponse(message, status));
    }

}
