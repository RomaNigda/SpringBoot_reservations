package org.example.springtest1.reservations.api;

public record ReservationSearchFilter(
        Long roomId,

        Long userId,

        Integer pageSize,

        Integer pageNumber
) {
}
