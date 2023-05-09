package com.example.tanso.consume.dto.response;

import com.example.tanso.consume.domain.model.Consumption;
import com.example.tanso.consume.enums.ConsumptionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsumptionRankingResponseDto {

    @ApiModelProperty(value = "유저 아이디")
    private long userId;

    @ApiModelProperty(value = "랭킹")
    private int rank;

    @ApiModelProperty(value = "수도/가스/전기 타입")
    private ConsumptionType type;

    @ApiModelProperty(value = "사용자 닉네임")
    private String nickname;

    @ApiModelProperty(value = "소비량")
    private int consumed;

    @Builder
    public ConsumptionRankingResponseDto(Consumption entity, String nickname, int rank) {
        this.type = entity.getType();
        this.userId = entity.getUserId();
        this.rank = rank;
        this.nickname = nickname;
        this.consumed = entity.getConsumed();
    }
}
