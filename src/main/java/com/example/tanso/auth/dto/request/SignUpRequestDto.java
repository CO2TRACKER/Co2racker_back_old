package com.example.tanso.auth.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    @ApiModelProperty(value = "유저 이메일")
    private String username;

    @ApiModelProperty(value = "패스워드")
    private String password;

    @ApiModelProperty(value = "닉네임")
    private String nickname;

//    @ApiModelProperty(value = "전기 고객 번호")
//    private String elecCustomerIdentifier;
//
//    @ApiModelProperty(value = "수도 고객 번호")
//    private String waterCustomerIdentifier;

    @ApiModelProperty(value = "도로명 주소")
    private String addr;

    @ApiModelProperty(value = "상세 주소")
    private String detailAddr;
}
