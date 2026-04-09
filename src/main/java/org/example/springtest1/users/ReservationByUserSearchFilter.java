package org.example.springtest1.users;

public record ReservationByUserSearchFilter(
        Long roomId,
        Integer pageSize,
        Integer pageNumber
) {
}
