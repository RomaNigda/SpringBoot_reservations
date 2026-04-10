package org.example.springtest1.users.api;

public record ReservationByUserSearchFilter(
        Long roomId,
        Integer pageSize,
        Integer pageNumber
) {
}
