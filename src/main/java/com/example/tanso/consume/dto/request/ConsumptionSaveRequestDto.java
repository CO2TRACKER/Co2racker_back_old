package com.example.tanso.consume.dto.request;

import com.example.tanso.consume.domain.model.Consumption;
import com.example.tanso.consume.enums.ConsumptionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Data
public class ConsumptionSaveRequestDto {

    @ApiModelProperty(value = "유저 아이디", hidden = true, example = "1")
    private long userId;

    @ApiModelProperty(value = "거주지", hidden = true, example = "강남구")
    private String area;

    @ApiModelProperty(value = "수도/가스/전기 타입", required = true, allowableValues = "GAS, ELEC, WATER")
    private ConsumptionType type;

    @ApiModelProperty(value = "소비량", required = true, example = "990")
    private int consumed;

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Consumption toEntity() {
        return Consumption.builder()
                .area(area)
                .userId(userId)
                .type(type)
                .consumed(consumed)
                .build();
    }
}
