package org.example.springtest1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ReservationService {
    public static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final Map<Long, Reservation> reservations;
    AtomicLong id;

    private final ReservationsRepository repository;


    @Autowired
    public ReservationService(ReservationsRepository repository) {
        this.repository = repository;
        this.reservations = new HashMap<>();
        this.id = new AtomicLong(1);
    }



    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<ReservationEntity> allEntities = repository.findAll();


        return ResponseEntity.ok(
                allEntities.stream()
                        .map(it -> new Reservation(
                                it.getId(),
                                it.getUserId(),
                                it.getRoomId(),
                                it.getStartDate(),
                                it.getEndDate(),
                                it.getStatus()
                        )).toList()
        );
    }

    public ResponseEntity<Reservation> getReservetionById(Long id) {
        if(reservations.containsKey(id)){
            return ResponseEntity.ok(reservations.get(id));
        }
        return null;
    }

    public ResponseEntity<Reservation> createReservation(
            Reservation reservation) {
        if(reservation.id() != null){
            throw new IllegalArgumentException("reservation id must be null");
        }
        if(reservation.status() != null){
            throw new IllegalArgumentException("reservation status must be null");
        }

        Reservation reservationToCreate = new Reservation(
                id.getAndAdd(1),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING);

        reservations.put(reservationToCreate.id(), reservationToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationToCreate);
    }

    public ResponseEntity<Reservation> deleteReservationById(Long id) {
        if (!reservations.containsKey(id)) {
            throw new IllegalArgumentException("reservation is not found, id=" + id);
        }
        return ResponseEntity.ok(reservations.remove(id));
    }

    public ResponseEntity<Reservation> updateReservation(
            Long id, Reservation reservation) {
        if (!reservations.containsKey(id)) {
            throw new IllegalArgumentException("reservation is not found, id=" + id);
        }
        if (reservation.id() != null) {
            throw new IllegalArgumentException("reservation id must be null");
        }
        if (reservation.status() != null && !reservation.status().equals(ReservationStatus.PENDING)) {
            throw new IllegalArgumentException("problem with status, id=" + id);
        }

        Reservation reservationToUpdate = new  Reservation(
                id,
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING
        );

        reservations.put(id, reservationToUpdate);
        return ResponseEntity.ok(reservationToUpdate);
    }

    public ResponseEntity<Reservation> approveReservationById(Long id) {
        if (!reservations.containsKey(id)) {
            throw new IllegalArgumentException("reservation is not found, id=" + id);
        }
        if (reservations.get(id).status() == ReservationStatus.CANCELLED ||
                reservations.get(id).status() == ReservationStatus.APPROVED) {
            throw new IllegalArgumentException("problem with status, id=" + id);
        }

        boolean hasConflict = isReservationValid(reservations.get(id));

        if (!hasConflict) {
            var reservationToUpdate = new Reservation(
                    id,
                    reservations.get(id).userId(),
                    reservations.get(id).roomId(),
                    reservations.get(id).startDate(),
                    reservations.get(id).endDate(),
                    ReservationStatus.APPROVED
            );
            reservations.put(id, reservationToUpdate);
            return ResponseEntity.ok(reservationToUpdate);
        } else {
            var reservationToUpdate = new Reservation(
                    id,
                    reservations.get(id).userId(),
                    reservations.get(id).roomId(),
                    reservations.get(id).startDate(),
                    reservations.get(id).endDate(),
                    ReservationStatus.CANCELLED
            );
            reservations.put(id, reservationToUpdate);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(reservationToUpdate);
        }
    }


    private boolean isReservationValid(Reservation reservation){
        return reservations.values()
                .stream()
                .filter((res) -> Objects.equals(res.roomId(), reservation.roomId()))
                .filter(res -> res.status() == ReservationStatus.APPROVED)
                .anyMatch((res) ->
                        reservation.startDate().isBefore(res.endDate()) &&
                                reservation.endDate().isAfter(res.startDate())
                );
    }
}
