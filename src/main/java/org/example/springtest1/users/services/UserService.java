package org.example.springtest1.users.services;

import jakarta.validation.Valid;
import org.example.springtest1.reservations.api.Reservation;
import org.example.springtest1.reservations.db.ReservationMapper;
import org.example.springtest1.reservations.db.ReservationsRepository;
import org.example.springtest1.reservations.service.ReservationService;
import org.example.springtest1.users.api.ReservationByUserSearchFilter;
import org.example.springtest1.users.db.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Objects;

@Service
public class UserService {
    private final ReservationService reservationService;
    private final ReservationsRepository reservationsRepository;
    private final ReservationMapper reservationMapper;

    public UserService(ReservationService reservationService, ReservationsRepository reservationsRepository, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.reservationsRepository = reservationsRepository;
        this.reservationMapper = reservationMapper;
    }


    public List<Reservation> loadReservationByUser(UserEntity user, ReservationByUserSearchFilter filter) {
        Long userId = user.getId();

        int pageSize = filter.pageSize() != null ? filter.pageSize() : 10;
        int pageNumber = filter.pageNumber() != null ? filter.pageNumber() : 0;
        Long roomId = filter.roomId() != null ? filter.roomId() : null;

        Pageable pageable = Pageable.ofSize(pageSize).withPage(pageNumber);

        List<Reservation> reservations = reservationsRepository
                .searchAllUserReservationsByFilter(userId, roomId, pageable)
                .stream()
                .map(reservationMapper::toDomain)
                .toList();

        return reservations;
    }

    public Reservation createReservation(
            @AuthenticationPrincipal UserEntity user,
            @Valid @RequestBody Reservation reservation
    ) {
        if (!Objects.equals(reservation.userId(), user.getId())) {
            throw new IllegalArgumentException("User can create reservations only for themselves");
        }

        Reservation reservationToReturn = reservationService.createReservation(reservation);

        return reservationToReturn;
    }
}
