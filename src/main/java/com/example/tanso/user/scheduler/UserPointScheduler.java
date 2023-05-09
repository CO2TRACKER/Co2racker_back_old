package com.example.tanso.user.scheduler;

import com.example.tanso.boot.utils.StringUtils;
import com.example.tanso.consume.domain.model.Consumption;
import com.example.tanso.consume.domain.repository.ConsumptionRepository;
import com.example.tanso.user.domain.model.User;
import com.example.tanso.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserPointScheduler {

    private final UserRepository userRepository;
    private final ConsumptionRepository consumptionRepository;

    // 매 자정마다 실행
    @Scheduled(cron = "0 0 0 * * *")
    public void pointSchedule() {
        // 오늘 및 다음 날 날짜
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime endOfToday = LocalDate.now().atTime(LocalTime.MAX.withNano(0));

        // 각 구 별 데이터를 모두 가져온 후, 해당 구에 속한 각 데이터를 가져온다.
        List<User> userEntityList = userRepository.findAll();
        List<String> userAddressList = userEntityList.stream()
                .map(User::getAddr)
                .collect(Collectors.toList());

        Set<String> guList = userAddressList.stream()
                .map(eachAddr -> new StringUtils().getGuFromAddress(eachAddr))
                .collect(Collectors.toSet());

        log.info(guList.toString());


        for (String eachGu : guList) {
            Map<Long, Double> todayConsumedRankMap = new HashMap<>();
            List<Consumption> todayConsumptionEntityList = consumptionRepository.findAllByAreaAndCreatedAtBetween(eachGu, today, endOfToday);

            // 각 유저 별 오늘 전체 소비량을 계산한다.
            for (Consumption eachConsumption : todayConsumptionEntityList) {
                // 만일, 소비 랭킹 맵에 현재 유저에 대한 시퀀스가 없을 경우 초기화한다.
                if (!todayConsumedRankMap.containsKey(eachConsumption.getUserId())) {
                    todayConsumedRankMap.put(eachConsumption.getUserId(), 0.0);
                }

                todayConsumedRankMap.compute(eachConsumption.getUserId(), (aLong, aDouble) -> aDouble + eachConsumption.getConsumed());
            }

            log.info(todayConsumedRankMap.toString());
        }
    }

}
