package org.example.springtest1.reservations;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReservationService {
    public static final Logger log = LoggerFactory.getLogger(ReservationService.class);
    private final ReservationsRepository repository;
    private final ReservationMapper mapper;

    @Autowired
    public ReservationService(ReservationsRepository repository,  ReservationMapper mapper) {
        this.mapper = mapper;
        this.repository = repository;
    }



    public List<Reservation> findReservationByFilter(
            ReservationSearchFilter filter)
    {
        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;

        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);



        Page<ReservationEntity> allEntities = repository.searchAllReservationsByFilter(
                filter.userId(),
                filter.roomId(),
                pageable
        );
        return allEntities.getContent().stream()
                        .map(mapper::toDomain).toList();
    }



    public Reservation getReservationById(Long id) {
        ReservationEntity reservation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation is not found, id=" + id));
        return mapper.toDomain(reservation);
    }



    public Reservation createReservation(
            Reservation reservation) {

        if(reservation.status() != null){
            throw new IllegalArgumentException("Reservation status must be null");
        }

        if (!reservation.endDate().isAfter(reservation.startDate())) {
            throw new IllegalArgumentException("Reservation start date must be after end date");
        }

        var reservationToCreate = mapper.toEntity(reservation);
        reservationToCreate.setId(null);
        reservationToCreate.setStatus(ReservationStatus.PENDING);



        var savedEntity = repository.save(reservationToCreate);
        return mapper.toDomain(savedEntity);
    }



    public void deleteReservationById(Long id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("reservation is not found, id=" + id);
        }

       repository.setStatus(id, ReservationStatus.CANCELLED);
    }



    public Reservation updateReservation(
            Long id, Reservation reservation) {

        var reservationToUpdate = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation is not found, id=" + id));

        if (reservationToUpdate.getStatus().equals(ReservationStatus.APPROVED)) {
            throw new IllegalStateException("Can`t cancel approved  reservation, please contact with manager");
        }

        if (reservationToUpdate.getStatus().equals(ReservationStatus.CANCELLED)) {
            throw new IllegalStateException("Reservation already cancelled");
        }

        var entityToSave = mapper.toEntity(reservation);
        entityToSave.setId(reservationToUpdate.getId());
        entityToSave.setStatus(ReservationStatus.PENDING);


        var updatedEntity = repository.save(entityToSave);
        return mapper.toDomain(updatedEntity);
    }



    public Reservation approveReservationById(Long id) {
        var reservation = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("reservation is not found, id=" + id));

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new IllegalStateException("problem with status, id=" + id);
        }


        List<Long> conflictedIds = repository.findAllConflictedId(
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                ReservationStatus.APPROVED);

        if (!conflictedIds.isEmpty()) {
            throw new IllegalArgumentException("These dates are already booked");
        }

        repository.setStatus(id, ReservationStatus.APPROVED);
        reservation.setStatus(ReservationStatus.APPROVED);

        return mapper.toDomain(reservation);
    }
}
