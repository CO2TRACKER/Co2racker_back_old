package com.example.tanso.consume.controller;

import com.example.tanso.consume.dto.request.ConsumptionSaveRequestDto;
import com.example.tanso.consume.dto.response.ConsumptionCo2;
import com.example.tanso.consume.dto.response.ConsumptionRankingResponseDto;
import com.example.tanso.consume.dto.response.ConsumptionResponseDto;
import com.example.tanso.consume.enums.ConsumptionType;
import com.example.tanso.consume.service.ConsumptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "[App] 소비 API", value = "전기/수도/가스 소비 관련 API")
@Slf4j
@RestController
@RequestMapping("/consumption")
@RequiredArgsConstructor
public class ConsumptionController {
    private final ConsumptionService consumptionService;

    @GetMapping("co2")
    @ApiOperation(value = "탄소발자국 평균값", notes = "현재 사용자의 전체 전기/수도/가스에 대한 탄소발자국을 계산한 평균치를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공")
    })
    public ResponseEntity<ConsumptionCo2> calcCo2(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization
    ) throws Exception {
        return new ResponseEntity<>(consumptionService.calcCo2(), HttpStatus.OK);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "전기/가스/수도 소비 등록 API", notes = "DTO 를 전달받아 해당하는 type 에 대한 소비량을 등록한다.\ntype 이 enum 에 정의된 이외의 값일 경우, 400 예외를 발생시킨다.")
    @ApiResponses({
            @ApiResponse(code = 201, message = "등록 성공"),
            @ApiResponse(code = 400, message = "등록 실패 - type 불일치")
    })
    public ResponseEntity<ConsumptionResponseDto> createConsumption(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestBody ConsumptionSaveRequestDto requestDto
    ) throws Exception {
        return new ResponseEntity<>(consumptionService.createConsumption(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("")
    @ApiOperation(value = "이번 달 소비량 조회 API", notes = "쿼리스트링을 통해 수도/전기/가스 등의 타입을 전달받아 해당하는 유저가 이번 달에 가장 마지막에 등록한 소비 데이터를 반환한다.\n이때 반횐되는 데이터는 이번달 소비량, 소비 절약 랭킹, 전월달 대비 소비랭킹, 금일 등록된 소비량 데이터, 현재 유저의 지역구(ex.송파구 등)에 속한 총 가구수에 대한 데이터이다.\n전월달 랭킹에 경우, 전월 달에 등록된 소비 데이터가 없을 경우(비교할 수 있는 데이터가 없을 경우) -1 을 반환한다.\n\n일치하는 데이터가 없을 경우, 404 예외를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
            @ApiResponse(code = 404, message = "조회 실패 - 데이터 없음")
    })
    public ResponseEntity<ConsumptionResponseDto> findConsumption(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestParam("type") ConsumptionType type
    ) throws Exception {
        return new ResponseEntity<>(consumptionService.findConsumption(type), HttpStatus.OK);
    }

    @GetMapping("/ranking")
    @ApiOperation(value = "실시간 사용 랭킹 조회 API", notes = "수도/가스/전기 등의 Enum 을 쿼리스트링으로 전달받아 이번 달에 해당하는 같은 구(강남구 등)의 전기/수도/가스 사용량 랭킹을 20위까지 조회한다.\n이때, 리스트의 첫 번째 인덱스부터 랭킹 1위를 의미한다.\n\n데이터가 없을 경우, 비어있는 리스트를 반환하며, 데이터가 존재할 경우 랭킹에 대한 리스트를 반환한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공"),
    })
    public ResponseEntity<List<ConsumptionRankingResponseDto>> findConsumptionRanking(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            @RequestParam("type") ConsumptionType type
    ) throws Exception {
        return new ResponseEntity<>(consumptionService.findConsumptionRanking(type), HttpStatus.OK);
    }

}
