package org.example.springtest1;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationsRepository extends JpaRepository<ReservationEntity, Long> {
}
