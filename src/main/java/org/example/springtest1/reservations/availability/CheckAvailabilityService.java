package org.example.springtest1.reservations.availability;


import org.example.springtest1.reservations.db.ReservationStatus;
import org.example.springtest1.reservations.db.ReservationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CheckAvailabilityService {
    private final Logger log = LoggerFactory.getLogger(CheckAvailabilityController.class);

    private final ReservationsRepository repository;


    public CheckAvailabilityService(ReservationsRepository repository) {
        this.repository = repository;
    }


    public boolean isReservationAvailable(
            Long roomId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalStateException("startDate is after endDate");
        }

        List<Long> conflictedIds = repository.findAllConflictedId(
                roomId,
                startDate,
                endDate,
                ReservationStatus.APPROVED);


        return conflictedIds.isEmpty();
    }
}
