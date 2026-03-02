package org.example.springtest1;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {
    public static final Logger log = LoggerFactory.getLogger(ReservationService.class);


    private final ReservationsRepository repository;


    @Autowired
    public ReservationService(ReservationsRepository repository) {
        this.repository = repository;
    }



    public List<Reservation> getAllReservations() {
        List<ReservationEntity> allEntities = repository.findAll();


        return allEntities.stream()
                        .map(it -> new Reservation(
                                it.getId(),
                                it.getUserId(),
                                it.getRoomId(),
                                it.getStartDate(),
                                it.getEndDate(),
                                it.getStatus()
                        )).toList();
    }



    public Reservation getReservetionById(Long id) {
        ReservationEntity reservation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation is not found, id=" + id));
        return toDomainReservation(reservation);
    }



    public Reservation createReservation(
            Reservation reservation) {
        if(reservation.id() != null){
            throw new IllegalArgumentException("reservation id must be null");
        }
        if(reservation.status() != null){
            throw new IllegalArgumentException("reservation status must be null");
        }

        var reservationToCreate = new ReservationEntity(
                null,
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING);

        var savedEntity = repository.save(reservationToCreate);

        return toDomainReservation(savedEntity);
    }



    public void deleteReservationById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("reservation is not found, id=" + id);
        }

        repository.deleteById(id);
    }



    public Reservation updateReservation(
            Long id, Reservation reservation) {

        var reservationToUpdate = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation is not found, id=" + id));

        if (reservationToUpdate.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("problem with status, id=" + id);
        }

        var entityToSave = new ReservationEntity(
                reservationToUpdate.getId(),
                reservation.userId(),
                reservation.roomId(),
                reservation.startDate(),
                reservation.endDate(),
                ReservationStatus.PENDING
        );

        var updatedEntity = repository.save(entityToSave);

        return toDomainReservation(updatedEntity);
    }



    public Reservation approveReservationById(Long id) {
        var reservation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation is not found, id=" + id));


        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("problem with status, id=" + id);
        }

        boolean hasConflict = isReservationValid(reservation);
        if (hasConflict) {
            throw new IllegalArgumentException("these dates are already booked, id = " + id);
        }

        reservation.setStatus(ReservationStatus.APPROVED);
        repository.save(reservation);


        return toDomainReservation(reservation);
    }






    private boolean isReservationValid(ReservationEntity reservation){
//        return reservations.values()
//                .stream()
//                .filter((res) -> Objects.equals(res.roomId(), reservation.getRoomId()))
//                .filter(res -> res.status() == ReservationStatus.APPROVED)
//                .anyMatch((res) ->
//                        reservation.getStartDate().isBefore(res.endDate()) &&
//                                reservation.endDate().isAfter(res.startDate())
//                );

        List<ReservationEntity> allReservations = repository.findAll();

        for (ReservationEntity existedEntity : allReservations) {
            if(existedEntity.getId().equals(reservation.getId())){ continue; }
            if(!existedEntity.getRoomId().equals(reservation.getRoomId())){ continue; }
            if(existedEntity.getStatus().equals(ReservationStatus.PENDING)){ continue; }

            if (reservation.getStartDate().isBefore(existedEntity.getEndDate()) &&
                                reservation.getEndDate().isAfter(existedEntity.getStartDate())){
                return true;
            }
        }

        return false;



    }



    private Reservation toDomainReservation(ReservationEntity reservation){
        return new Reservation(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus());
    }
}
