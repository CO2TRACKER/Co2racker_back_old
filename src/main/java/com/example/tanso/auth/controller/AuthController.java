package com.example.tanso.auth.controller;

import com.example.tanso.auth.dto.request.SignInRequestDto;
import com.example.tanso.auth.dto.request.SignUpRequestDto;
import com.example.tanso.auth.dto.response.SignInResponseDto;
import com.example.tanso.auth.service.AuthService;
import com.example.tanso.user.dto.response.UserDetailsImpl;
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

@Api(tags = {"[App] 인증 API"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authservice;

    @PostMapping("/sign-in")
    @ApiOperation(value = "로그인 API", notes = "로그인을 수행한다. 만일 로그인에 실패할 경우 401 에러가 반환된다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 성공", response = SignInResponseDto.class),
            @ApiResponse(code = 401, message = "로그인 실패")
    })
    public ResponseEntity<SignInResponseDto> signIn(@RequestBody SignInRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(authservice.signIn(requestDto), HttpStatus.OK);
    }

    @PostMapping("/sign-out")
    @ApiOperation(value = "로그아웃 API", notes = "로그아웃을 수행한다. Security Context 에서 해당 정보를 모두 제거하고, DB 내 Auth Token 데이터를 지운다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그아웃 성공", response = UserDetailsImpl.class)
    })
    public ResponseEntity<Boolean> signOut(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        return new ResponseEntity<>(authservice.signOut(), HttpStatus.OK);
    }

    @PostMapping("/sign-up")
    @ApiOperation(value = "일반 회원가입 API", notes = "DTO 를 전달받아 회원가입을 수행한다. 이때, 기본 권한은 일반 유저로 등록된다.<br/>휴대폰 번호는 반드시 하이픈(-)이 제외되어야 한다.")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses({
            @ApiResponse(code = 201, message = "생성 성공", response = UserDetailsImpl.class),
            @ApiResponse(code = 400, message = "생성 실패 - 기존재 아이디 혹은 휴대폰 번호")
    })
    public ResponseEntity<UserDetailsImpl> signUp(@RequestBody SignUpRequestDto requestDto) throws Exception {
        return new ResponseEntity<>(authservice.signUp(requestDto), HttpStatus.CREATED);
    }
}
