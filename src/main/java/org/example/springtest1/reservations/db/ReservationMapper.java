package org.example.springtest1.reservations.db;

import org.example.springtest1.reservations.api.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {
    public Reservation toDomain(ReservationEntity reservation) {
        return new Reservation(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getRoomId(),
                reservation.getStartDate(),
                reservation.getEndDate(),
                reservation.getStatus()
        );
    }

    public ReservationEntity toEntity(Reservation domain) {
        return new ReservationEntity(
                domain.id(),
                domain.userId(),
                domain.roomId(),
                domain.startDate(),
                domain.endDate(),
                domain.status()
        );
    }
}
