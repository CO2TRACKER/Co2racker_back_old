package com.example.tanso.auth.service;

import com.example.tanso.auth.dto.request.SignInRequestDto;
import com.example.tanso.auth.dto.request.SignUpRequestDto;
import com.example.tanso.auth.dto.response.SignInResponseDto;
import com.example.tanso.user.dto.response.UserDetailsImpl;

public interface AuthService {
    SignInResponseDto signIn(SignInRequestDto requestDto) throws Exception;
    Boolean signOut() throws Exception;
    UserDetailsImpl signUp(SignUpRequestDto requestDto) throws Exception;
}
