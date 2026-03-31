package org.example.springtest1.reservations.availability;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CheckAvailabilityRequest(
        @NotNull
        Long roomId,

        @FutureOrPresent
        LocalDate startDate,

        @FutureOrPresent
        LocalDate endDate
) {
}
