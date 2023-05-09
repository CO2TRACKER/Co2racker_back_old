package com.example.tanso.example.controller;

import com.example.tanso.example.dto.request.ExampleSaveRequestDto;
import com.example.tanso.example.dto.response.ExampleResponseDto;
import com.example.tanso.example.service.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "연동용 API")
@Slf4j
@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;

    @PostMapping("")
    @ApiOperation(value = "예제 생성 API", notes = "Body 를 전달받아 예제 데이터를 생성합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "생성 성공")
    })
    public ResponseEntity<ExampleResponseDto> createExample(@RequestBody ExampleSaveRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(exampleService.createExample(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("")
        @ApiOperation(value = "예제 전체 조회 API", notes = "데이터베이스 내에 존재하는 모든 예제 데이터를 조회합니다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공")
    })
    public ResponseEntity<List<ExampleResponseDto>> findAllExamples() throws Exception {
        return new ResponseEntity<>(exampleService.findAllExamples(), HttpStatus.OK);
    }
}
