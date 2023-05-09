package com.example.tanso.user.service;


import com.example.tanso.user.dto.request.UserInfoUpdateRequestDto;
import com.example.tanso.user.dto.response.UserDetailsImpl;
import com.example.tanso.user.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserDetailsImpl findUserInfo() throws Exception;
}
