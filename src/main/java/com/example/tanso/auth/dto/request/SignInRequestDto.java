package com.example.tanso.auth.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInRequestDto {

    private String username;
    private String password;

}
