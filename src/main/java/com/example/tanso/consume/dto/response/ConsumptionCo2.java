package com.example.tanso.consume.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsumptionCo2 {
    @ApiModelProperty(value = "전기 탄소발자국 평균값", example = "14.2", required = true)
    private double elecCo2;

    @ApiModelProperty(value = "가스 탄소발자국 평균값", example = "14.2", required = true)
    private double gasCo2;

    @ApiModelProperty(value = "수도 탄소발자국 평균값", example = "14.2", required = true)
    private double waterCo2;

    @ApiModelProperty(value = "전체 탄소발자국 평균값", example = "14.2", required = true)
    private double totalCo2;
}
