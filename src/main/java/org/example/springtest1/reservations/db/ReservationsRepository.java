package org.example.springtest1.reservations.db;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservationsRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("select r from ReservationEntity r where r.id = :id")
    public List<ReservationEntity> findByIdTest(@Param("id") long id);

    @Modifying
    @Transactional
    @Query("update ReservationEntity r set r.status = :status where r.id = :id")
    public void setStatus(
            @Param("id") Long id,
            @Param("status") ReservationStatus status);

    @Query("""
        SELECT r.id from ReservationEntity r
                WHERE r.roomId = :roomId
                AND r.status = :status
                AND r.startDate < :endDate
                AND r.endDate > :startDate
        """)
    public List<Long> findAllConflictedId(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("status")  ReservationStatus status
    );



    @Query("""
        SELECT r from ReservationEntity r
                WHERE (:roomId IS NULL OR r.roomId = :roomId)
                AND (:userId IS NULL OR r.userId = :userId)
        """)
    Page<ReservationEntity> searchAllReservationsByFilter(
            @Param("userId")  Long userId,
            @Param("roomId") Long roomId,
            Pageable pageable
    );





}
