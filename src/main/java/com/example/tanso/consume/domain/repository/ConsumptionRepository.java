package com.example.tanso.consume.domain.repository;

import com.example.tanso.consume.domain.model.Consumption;
import com.example.tanso.consume.dto.response.ConsumptionJQPL;
import com.example.tanso.consume.enums.ConsumptionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
    Optional<Consumption> findTopByUserIdAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(long userId, ConsumptionType type, LocalDateTime startDate, LocalDateTime endDate);

    void deleteBySeq(long seq);

    // 실시간 소비 랭킹 조회
    List<Consumption> findByTypeAndAreaAndCreatedAtBetweenOrderByConsumedDesc(ConsumptionType type, String area, LocalDateTime startDate, LocalDateTime endDate);

    // 특정 유저의 특정 기간 소비데이터 조회
    List<Consumption> findByUserIdAndCreatedAtBetween(long userId, LocalDateTime startDate, LocalDateTime endDate);

    // 유저의 가스/전기/수도 전체 데이터 조회
    @Query(value = "SELECT DISTINCT(DATE_FORMAT(c.created_at, '%Y-%m-%d')) as createdAt, c.user_id as userId, MAX(c.consumed) as consumed, c.type as type " +
            "FROM tb_comsumption as c " +
            "WHERE user_id=:userId " +
            "GROUP BY createdAt, type", nativeQuery = true)
    List<ConsumptionJQPL> findUsingJQPL(long userId);

    // 지역구 별 데이터 가져오기
    List<Consumption> findAllByAreaAndCreatedAtBetween(String area, LocalDateTime startDate, LocalDateTime endDate);
}
