package com.example.tanso.user.dto.response;

import com.example.tanso.user.domain.model.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class UserResponseDto {

    @ApiModelProperty(value = "시퀀스")
    private Long id;

    @ApiModelProperty(value = "회원 이메일")
    private String username;

    @ApiModelProperty(value = "유저 닉네임")
    private String nickname;
//    @ApiModelProperty(value = "전기고객번호")
//    private String elecCustomerIdentifier;
//    @ApiModelProperty(value = "수도고객번호")
//    private String waterCustomerIdentifier;
    @ApiModelProperty(value = "도로명 주소")
    private String addr;
    @ApiModelProperty(value = "상세 주소")
    private String detailAddr;

    @ApiModelProperty(value = "포인트")
    private long point;

    @Builder
    public UserResponseDto(User entity) {
        this.id = entity.getId();
        this.username = entity.getUsername();
        this.nickname = entity.getNickname();
//        this.elecCustomerIdentifier = entity.getElecCustomerIdentifier();
//        this.waterCustomerIdentifier = entity.getWaterCustomerIdentifier();
        this.addr = entity.getAddr();
        this.detailAddr = entity.getDetailAddr();
        this.point = entity.getPoint();
    }

}
