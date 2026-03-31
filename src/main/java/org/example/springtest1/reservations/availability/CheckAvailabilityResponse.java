package org.example.springtest1.reservations.availability;

public record CheckAvailabilityResponse (
        String message,
        CheckAvailabilityStatus status
) {
}
