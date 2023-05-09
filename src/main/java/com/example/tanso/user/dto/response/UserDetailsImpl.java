package com.example.tanso.user.dto.response;

import com.example.tanso.user.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsImpl implements UserDetails {
    @ApiModelProperty(value = "유저 시퀀스")
    private Long id;
    @ApiModelProperty(value = "유저 이메일")
    private String username;
    @JsonIgnore
    private String password;
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
    @ApiModelProperty(value = "유저 권한")
    private Collection<? extends GrantedAuthority> authorities;

    @ApiModelProperty(value = "포인트")
    private long point;

    public static UserDetailsImpl getUserDetails(User entity) {
        return UserDetailsImpl.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .nickname(entity.getNickname())
//                .elecCustomerIdentifier(entity.getElecCustomerIdentifier())
//                .waterCustomerIdentifier(entity.getWaterCustomerIdentifier())
                .addr(entity.getAddr())
                .detailAddr(entity.getDetailAddr())
                .point(entity.getPoint())
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
