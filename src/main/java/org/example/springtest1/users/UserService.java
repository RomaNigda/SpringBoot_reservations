package org.example.springtest1.users;

import org.example.springtest1.reservations.api.Reservation;
import org.example.springtest1.reservations.db.ReservationMapper;
import org.example.springtest1.reservations.db.ReservationsRepository;
import org.example.springtest1.reservations.service.ReservationService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final ReservationsRepository reservationsRepository;
    private final ReservationMapper reservationMapper;

    public UserService(ReservationsRepository reservationsRepository, ReservationMapper reservationMapper) {
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
}
