package org.example.springtest1;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationsRepository extends JpaRepository<ReservationEntity, Long> {


    @Query("select r from ReservationEntity r where r.id = :id")
    public List<ReservationEntity> findByIdTest(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("update ReservationEntity r set r.status = :status where r.id = :id")
    public void cancelReservation(
            @Param("id") long id,
            @Param("status") ReservationStatus status);




}
