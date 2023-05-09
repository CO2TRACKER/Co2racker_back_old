package com.example.tanso.user.controller;

import com.example.tanso.user.dto.response.UserDetailsImpl;
import com.example.tanso.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"[App] 유저 API"})
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/info")
    @ApiOperation(value = "유저 정보 조회 API", notes = "Authorization 헤더를 통해 유저 Access Token 을 전달받아 해당하는 유저의 정보를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "조회 성공", response = UserDetailsImpl.class)
    })
    public ResponseEntity<UserDetailsImpl> findUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization) throws Exception {
        return new ResponseEntity<>(userService.findUserInfo(), HttpStatus.OK);
    }
}
