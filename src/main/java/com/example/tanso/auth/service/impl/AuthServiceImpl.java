package com.example.tanso.auth.service.impl;

import com.example.tanso.auth.domain.model.AuthToken;
import com.example.tanso.auth.domain.repository.AuthTokenRepository;
import com.example.tanso.auth.domain.repository.RolesRepository;
import com.example.tanso.auth.dto.request.SignInRequestDto;
import com.example.tanso.auth.dto.request.SignUpRequestDto;
import com.example.tanso.auth.dto.response.SignInResponseDto;
import com.example.tanso.auth.service.AuthService;
import com.example.tanso.boot.exception.RestException;
import com.example.tanso.boot.utils.JwtUtils;
import com.example.tanso.user.domain.model.User;
import com.example.tanso.user.domain.repository.UserRepository;
import com.example.tanso.user.dto.response.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthTokenRepository authTokenRepository;
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public SignInResponseDto signIn(SignInRequestDto requestDto) throws Exception {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String token = jwtUtils.generateJwtToken(authentication);

        /**
         * 로그인 토큰을 DB 에 저장한다.
         * 만일, 이미 중복된 사용자의 기록이 있을 경우 덮어씌우기한다.
         */
        if(authTokenRepository.existsByUserId(userDetails.getId())) {
            log.info("기로그인된 사용자입니다. DB 내 Access Token 갱신을 수행합니다. username=" + userDetails.getUsername());
            AuthToken authTokenEntity = authTokenRepository.findByUserId(userDetails.getId());
            authTokenEntity.updateAccessToken(token);
        } else {
            AuthToken authTokenEntity = AuthToken.builder()
                    .accessToken(token)
                    .userId(userDetails.getId())
                    .build();

            authTokenRepository.save(authTokenEntity);
        }

        return SignInResponseDto.builder()
                .userId(userDetails.getId())
                .accessToken(token)
                .build();
    }

    @Override
    public Boolean signOut() throws Exception {
        // Security Context 를 지운다.
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecurityContextHolder.clearContext();

        // 기존 DB 에 저장되어있던 데이터를 제거한다.
        authTokenRepository.deleteByUserId(userDetails.getId());

        return true;
    }

    @Override
    @Transactional
    public UserDetailsImpl signUp(SignUpRequestDto requestDto) throws Exception {
        // 현재 존재하는 회원인지 확인한다.
        if(userRepository.existsByUsername(requestDto.getUsername()))
            throw new RestException(HttpStatus.BAD_REQUEST, "이미 존재하는 유저 아이디입니다. username=" + requestDto.getUsername());

        // 유저를 생성한다.
        User user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .nickname(requestDto.getNickname())
//                .elecCustomerIdentifier(requestDto.getElecCustomerIdentifier())
//                .waterCustomerIdentifier(requestDto.getWaterCustomerIdentifier())
                .addr(requestDto.getAddr())
                .detailAddr(requestDto.getDetailAddr())
                .build();

        User userEntity = userRepository.save(user);

        return UserDetailsImpl.getUserDetails(userEntity);
    }
}
