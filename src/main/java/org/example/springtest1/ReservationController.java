package org.example.springtest1;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(reservationService.getReservetionById(id));
        } catch (EntityNotFoundException e) {
            log.info(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(
            @RequestBody Reservation reservation) {
        try{
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(reservationService.createReservation(reservation));
        } catch (IllegalArgumentException e){
            log.info("Create reservation failed, problem: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    

    @DeleteMapping("/{id}")
    public ResponseEntity<Reservation> deleteReservationById(
            @PathVariable Long id) {
        try {
            reservationService.deleteReservationById(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e){
            log.info("Delete reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
            @PathVariable Long id,
            @RequestBody Reservation reservation) {
        try{
            return ResponseEntity.ok(reservationService.updateReservation(id, reservation));
        } catch (IllegalStateException e){
            log.info("Update reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (EntityNotFoundException e){
            log.info("Update reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservationById(
            @PathVariable Long id) {
        try{
            return ResponseEntity.ok(reservationService.approveReservationById(id));
        } catch (EntityNotFoundException e){
            log.info("Approve reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e){
            log.info("Approve reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (IllegalArgumentException e){
            log.info("Approve reservation by id={} failed, problem: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


}
