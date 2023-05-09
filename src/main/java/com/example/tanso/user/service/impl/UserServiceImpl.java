package com.example.tanso.user.service.impl;

import com.example.tanso.auth.domain.repository.AuthTokenRepository;
import com.example.tanso.boot.exception.RestException;
import com.example.tanso.user.domain.model.User;
import com.example.tanso.user.domain.repository.UserRepository;
import com.example.tanso.user.dto.request.UserInfoUpdateRequestDto;
import com.example.tanso.user.dto.response.UserDetailsImpl;
import com.example.tanso.user.dto.response.UserResponseDto;
import com.example.tanso.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthTokenRepository authTokenRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 해당 요청은 ROLE_USER 권한을 지닌 사람만 호출이 가능한 API 이다.
     * 권한 정의는 SecurityConfig.java 에서 지정한다.
     * Filter(AuthJwtFilter.java) 에 요청(with Authorization Token, 즉 Access Token 으로 전달받음.)이 유효하다면,
     * Spring Security Context Holder 에 해당 유저의 정보가 삽입되었을 것이므로 해당 Context Holder 를 재활용하여 유저 정보를 추출해낸다.
     * @throws Exception
     */
    @Override
    @Transactional
    public UserDetailsImpl findUserInfo() throws Exception {
        // Security Context Holder 에서 유저 정보를 추출하여 반환한다.
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
