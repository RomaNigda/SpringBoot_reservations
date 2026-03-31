package org.example.springtest1.reservations.api;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import org.example.springtest1.reservations.db.ReservationStatus;

import java.time.LocalDate;

public record Reservation(
        @Null
        Long id,

        @NotNull
        Long userId,

        @NotNull
        Long roomId,

        @FutureOrPresent
        LocalDate startDate,

        @Future
        LocalDate endDate,

        ReservationStatus status
) {
}
