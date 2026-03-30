package org.example.springtest1.reservations;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("reservation/v1")
public class ReservationController {
    public static final Logger log = LoggerFactory.getLogger(ReservationController.class);
    public final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }


    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations(
            @RequestParam(name = "roomId", required = false) Long roomId,
            @RequestParam(name = "userId", required = false) Long userId,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
            ) {
        var filter = new ReservationSearchFilter(
                roomId,
                userId,
                pageSize,
                pageNumber);

        return ResponseEntity.ok(
                reservationService.findReservationByFilter(filter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @Valid @RequestBody Reservation reservation) {
        return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reservationService.createReservation(reservation));
    }


    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<Void> deleteReservationById(
            @PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.ok().build();

    }


    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationService.updateReservation(id, reservation));
    }


    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservationById(
            @PathVariable Long id) {
            return ResponseEntity.ok(reservationService.approveReservationById(id));
    }


}
