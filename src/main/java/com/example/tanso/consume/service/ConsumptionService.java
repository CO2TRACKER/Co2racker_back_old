package com.example.tanso.consume.service;

import com.example.tanso.boot.exception.RestException;
import com.example.tanso.boot.utils.StringUtils;
import com.example.tanso.consume.domain.model.Consumption;
import com.example.tanso.consume.domain.repository.ConsumptionRepository;
import com.example.tanso.consume.dto.request.ConsumptionSaveRequestDto;
import com.example.tanso.consume.dto.response.ConsumptionCo2;
import com.example.tanso.consume.dto.response.ConsumptionJQPL;
import com.example.tanso.consume.dto.response.ConsumptionRankingResponseDto;
import com.example.tanso.consume.dto.response.ConsumptionResponseDto;
import com.example.tanso.consume.enums.ConsumptionType;
import com.example.tanso.user.domain.model.User;
import com.example.tanso.user.domain.repository.UserRepository;
import com.example.tanso.user.dto.response.UserDetailsImpl;
import com.example.tanso.user.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumptionService {

    private final ConsumptionRepository consumptionRepository;
    private final UserRepository userRepository;

    /**
     * 현재 사용자에 대한 전체 전기/수도/가스에 대한 탄소발자국을 계산한다.
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public ConsumptionCo2 calcCo2() throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userDetails.getId();

        List<ConsumptionJQPL> co2List = consumptionRepository.findUsingJQPL(userId);

        double elecCo2 = 0.0;
        double gasCo2 = 0.0;
        double waterCo2 = 0.0;
        double totalCo2 = 0.0;

        // 전기 탄소 발자국을 계산한다.
        List<ConsumptionJQPL> elecCo2List = co2List.stream().filter(eachCo2 -> eachCo2.getType() == 1).collect(Collectors.toList());
        for (ConsumptionJQPL each : elecCo2List) {
            elecCo2 += each.getConsumed();
        }

        elecCo2 /= elecCo2List.size();

        if (Double.isNaN(elecCo2)) {
            elecCo2 = 0.0;
        }

        // 가스 탄소 발자국을 계산한다.
        List<ConsumptionJQPL> gasCo2List = co2List.stream().filter(eachCo2 -> eachCo2.getType() == 2).collect(Collectors.toList());
        for (ConsumptionJQPL each : gasCo2List) {
            gasCo2 += each.getConsumed();
        }

        gasCo2 /= gasCo2List.size();

        if (Double.isNaN(gasCo2)) {
            gasCo2 = 0.0;
        }

        // 수도 탄소 발자국을 계산한다.
        List<ConsumptionJQPL> waterCo2List = co2List.stream().filter(eachCo2 -> eachCo2.getType() == 2).collect(Collectors.toList());
        for (ConsumptionJQPL each : waterCo2List) {
            waterCo2 += each.getConsumed();
        }

        waterCo2 /= waterCo2List.size();

        if (Double.isNaN(waterCo2)) {
            waterCo2 = 0.0;
        }

        // 전체 탄소 발자국을 계산한다.
        for (ConsumptionJQPL each : co2List) {
            totalCo2 += each.getConsumed();
        }

        totalCo2 /= co2List.size();

        if (Double.isNaN(totalCo2)) {
            totalCo2 = 0.0;
        }

        return ConsumptionCo2.builder()
                .elecCo2(elecCo2)
                .gasCo2(gasCo2)
                .waterCo2(waterCo2)
                .totalCo2(totalCo2)
                .build();
    }

    @Transactional
    public ConsumptionResponseDto createConsumption(ConsumptionSaveRequestDto requestDto) throws Exception {
        // 유저 정보를 바탕으로 DTO 에 거주지를 삽입한다.
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userDetails.getId();
        String gu = new StringUtils().getGuFromAddress(userDetails.getAddr());

        requestDto.setArea(gu);
        requestDto.setUserId(userId);

        // 만일, 현재 삽입하려는 데이터가 기존에 삽입한 이번 달 소비량보다 작을 경우 예외를 발생시킨다.
        try {
            ConsumptionResponseDto consumptionResponseDto = findConsumption(requestDto.getType());

            if (consumptionResponseDto.getConsumed() > requestDto.getConsumed()) {
                throw new RestException(HttpStatus.BAD_REQUEST, "기존에 등록된 소비량보다 작은 소비량을 입력할 수 없습니다.");
            }
        } catch(RestException exception) {
            if (exception.getHttpStatus().equals(HttpStatus.BAD_REQUEST)) {
                throw new RestException(HttpStatus.BAD_REQUEST, "기존에 등록된 소비량보다 작은 소비량을 입력할 수 없습니다.");
            }
        }

        Consumption entity = consumptionRepository.save(requestDto.toEntity());

        return ConsumptionResponseDto.builder().entity(entity).build();
    }

    @Transactional(readOnly = true)
    public ConsumptionResponseDto findConsumption(ConsumptionType type) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userDetails.getId();

        // 이번 달을 기준으로 이번 달의 시작과 끝을 구한다.
        LocalDateTime now = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(LocalTime.MAX.withNano(0));
        LocalDateTime startDate = now.withDayOfMonth(1);
        LocalDateTime endDate = startDate.plusMonths(1);

        log.info(String.valueOf(userId));

        Consumption entity = consumptionRepository.findTopByUserIdAndTypeAndCreatedAtBetweenOrderByCreatedAtDesc(userId, type, startDate, endDate).orElseThrow(
                () -> new RestException(HttpStatus.NOT_FOUND, "일치하는 데이터를 찾을 수 없습니다.")
        );

        // 만일 조회한 데이터의 지역구와 현재 유저의 지역구가 일치하지 않을 경우 예외를 발생시킨다.
        if (!new StringUtils().getGuFromAddress(userDetails.getAddr()).equals(entity.getArea())) {
            throw new RestException(HttpStatus.NOT_FOUND, "일치하는 데이터를 찾을 수 없습니다.");
        }

        // Response DTO 를 생성한 후, 유저의 실명을 포함하여 반환한다.
        ConsumptionResponseDto responseDto = ConsumptionResponseDto.builder().entity(entity).build();
        responseDto.setNickname(userDetails.getNickname());

        // 해당 유저의 이번 달 랭킹을 구한다.
        List<ConsumptionRankingResponseDto> consumptionRankingDtoList = findConsumptionRanking(type);

        int userRanking = consumptionRankingDtoList.stream()
                .filter(eachRanking -> eachRanking.getUserId() == userId)
                .collect(Collectors.toList()).get(0).getRank();
        responseDto.setRank(userRanking);
        responseDto.setTotalHouseholds(consumptionRankingDtoList.size());

        // 금일 사용량 데이터를 구한다.
        // 존재하지 않을 경우, 빈 리스트를 삽입한다.
        List<ConsumptionResponseDto> todayResponseDto = consumptionRepository.findByUserIdAndCreatedAtBetween(userId, now, endOfDay)
                .stream()
                .map(ConsumptionResponseDto::new)
                .collect(Collectors.toList());

        List<ConsumptionResponseDto.TodayConsumptionGraph> todayResponseDtoList = new ArrayList<>();

        for (ConsumptionResponseDto eachTodayDto : todayResponseDto) {
            todayResponseDtoList.add(
                    ConsumptionResponseDto.TodayConsumptionGraph.builder()
                            .createdAt(eachTodayDto.getCreatedAt())
                            .consumed(eachTodayDto.getConsumed())
                            .build()
            );
        }

        responseDto.setTodayConsumedGraphData(
                todayResponseDtoList
        );

        // 전월달 대비 랭킹을 연산한다.
        // 1. 해당 유저의 userId 를 통해 전월달 데이터가 있는지 확인한다. 만일, 존재하지 않을 경우 -1 을 반환한다.
        // 2. 전월달 데이터가 존재할 경우, 해당 유저의 지역구에 해당하는 전체 유저들의 전월달과 이번달 사용량을 계산한다.
        // 3. DTO 에 전월달 대비 랭킹에 대한 정수값을 담아서 반환한다.

        // 전월달 데이터를 조회하기 위한 LocalDateTime
        LocalDateTime previousMonth = LocalDate.now().withDayOfMonth(1).minusMonths(1).atStartOfDay();
        LocalDateTime currentMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        List<ConsumptionResponseDto> previousDataDto = consumptionRepository.findByUserIdAndCreatedAtBetween(userId, previousMonth, currentMonth)
                .stream()
                .map(ConsumptionResponseDto::new)
                .collect(Collectors.toList());

        if (!previousDataDto.isEmpty()) {
            // 현재 사용자의 지역구를 추출한다. (ex. 송파구)
            String gu = new StringUtils().getGuFromAddress(userDetails.getAddr());

            // 현재 지역구에 대한 전월달 데이터들을 조회한다.
            List<ConsumptionResponseDto> previousMonthConsumptionRankDtoList = consumptionRepository.findByTypeAndAreaAndCreatedAtBetweenOrderByConsumedDesc(type, gu, previousMonth, currentMonth)
                    .stream()
                    .map(ConsumptionResponseDto::new)
                    .collect(Collectors.toList());

            List<ConsumptionResponseDto> revisedPrevious = new ArrayList<>();

            for (ConsumptionResponseDto eachPrevious : previousMonthConsumptionRankDtoList) {
                long previousUserId = eachPrevious.getUserId();
                if (revisedPrevious.stream().anyMatch(eachElement -> eachElement.getUserId() == previousUserId)) {
                    continue;
                }

                revisedPrevious.add(eachPrevious);
            }

            // 현재 지역구에 대한 현재 랭킹 데이터들을 조회한다.
            List<ConsumptionResponseDto> currentMonthConsumptionRankDtoList = consumptionRepository.findByTypeAndAreaAndCreatedAtBetweenOrderByConsumedDesc(type, gu, startDate, endDate)
                    .stream()
                    .map(ConsumptionResponseDto::new)
                    .collect(Collectors.toList());

            List<ConsumptionResponseDto> revisedCurrent = new ArrayList<>();

            for (ConsumptionResponseDto eachCurrent : currentMonthConsumptionRankDtoList) {
                long currentUserId = eachCurrent.getUserId();
                if (revisedCurrent.stream().anyMatch(eachElement -> eachElement.getUserId() == currentUserId)) {
                    continue;
                }

                revisedCurrent.add(eachCurrent);
            }

            // 조회된 두 데이터 사이에 존재하는 userId 들에 대하여 랭킹을 연산한다.
            Map<Long, Integer> previousRankingMap = new HashMap<>();
            for (ConsumptionResponseDto eachPrevious : revisedPrevious) {
                ConsumptionResponseDto eachCurrentDto = revisedCurrent.stream().filter(eachCurrent -> eachCurrent.getUserId() == eachPrevious.getUserId()).findFirst()
                        .orElse(null);

                if (eachCurrentDto == null) {
                    continue;
                }

                // 전월달 대비 이번 달 사용량의 차를 계산한다.
                int currentConsumptionAgainstPrevious = eachCurrentDto.getConsumed() - eachPrevious.getConsumed();

                previousRankingMap.put(eachCurrentDto.getUserId(), currentConsumptionAgainstPrevious);
            }

            // 산출된 Map 을 바탕으로 Value 별로 정렬한다. (순위)
            List<Map.Entry<Long, Integer>> entries = previousRankingMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue())
                                    .collect(Collectors.toList());

            for (int i=entries.size(); i>=1; i--) {
                entries.get(i - 1).setValue(i);
            }

            // 현재 유저에 대한 전월달 대비를 Entry 에서 검색하여 해당하는 value(rank) 를 전월달 대비 랭킹에 삽입한다.
            Map.Entry<Long, Integer> foundPreviousRank = entries.stream().filter(eachEntry -> eachEntry.getKey() == userId).findFirst()
                    .orElse(null);

            if (foundPreviousRank == null) {
                throw new RestException(HttpStatus.INTERNAL_SERVER_ERROR, "오류가 발생했습니다.");
            }

            responseDto.setRankAgainstPrevMonth(foundPreviousRank.getValue());
        } else {
            responseDto.setRankAgainstPrevMonth(-1);
        }

        return responseDto;
    }

    /**
     * 이번 달 사용 랭킹을 구한다.
     * 동일 유저에 대하여 여러 데이터가 존재할 경우, 가장 최신 데이터를 구하여 연산한다.
     * @param type 소비 타입
     * @return 사용 랭킹 DTO를 반환한다.
     * @throws Exception 예외
     */
    public List<ConsumptionRankingResponseDto> findConsumptionRanking(ConsumptionType type) throws Exception {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String gu = new StringUtils().getGuFromAddress(userDetails.getAddr());

        // 이번 달을 기준으로 이번 달의 시작과 끝을 구한다.
        LocalDateTime now = LocalDate.now().atStartOfDay();
        LocalDateTime startDate = now.withDayOfMonth(1);
        LocalDateTime endDate = startDate.plusMonths(1);

        List<Consumption> consumptionEntityList = consumptionRepository.findByTypeAndAreaAndCreatedAtBetweenOrderByConsumedDesc(type, gu, startDate, endDate);

        // 각 소비량에 대한 유저 닉네임을 구한 후, 각 ResponseDto 에 삽입한다.
        List<ConsumptionRankingResponseDto> responseDtoList = new ArrayList<>();
        int rank = 1;
        for (Consumption eachEntity : consumptionEntityList) {
            long userId = eachEntity.getUserId();

            // 이미 responseDtoList 에 동일 유저 아이디가 존재할 경우, 건너뛴다.
            if (responseDtoList.stream().anyMatch(eachDto -> eachDto.getUserId() == userId)) {
                continue;
            }

            User userEntity = userRepository.findById(userId).orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "일치하는 유저를 찾을 수 없습니다. userId=" + userId));
            UserResponseDto userResponseDto = UserResponseDto.builder().entity(userEntity).build();
            String nickname = userResponseDto.getNickname();

            responseDtoList.add(
                    ConsumptionRankingResponseDto.builder()
                            .entity(eachEntity)
                            .nickname(nickname)
                            .rank(rank)
                            .build()
            );

            rank += 1;
        }

        // 랭킹 데이터를 조회 및 반환한다.
        return responseDtoList;
    }
}
