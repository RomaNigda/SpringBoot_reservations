package org.example.springtest1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<Reservation>> getAllReservations() {
//        System.out.println("reservation");
        return reservationService.getAllReservations();
//        return "hello";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id) {
        return reservationService.getReservetionById(id);
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation) {
        try{
            return reservationService.createReservation(reservation);
        } catch (IllegalArgumentException e){
            log.info("Create reservation failed, problem: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    

    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> deleteReservationById(
            @PathVariable Long id) {
        try {
            return reservationService.deleteReservationById(id);
        } catch (IllegalArgumentException e){
            log.info("Delete reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        try{
            return reservationService.updateReservation(id, reservation);
        } catch (IllegalArgumentException e){
            log.info("Update reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservationById(
            @PathVariable Long id) {
        try{
            return reservationService.approveReservationById(id);
        } catch (IllegalArgumentException e){
            log.info("Approve reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


}
