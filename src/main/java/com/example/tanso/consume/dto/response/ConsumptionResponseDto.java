package com.example.tanso.consume.dto.response;

import com.example.tanso.consume.domain.model.Consumption;
import com.example.tanso.consume.enums.ConsumptionType;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor
public class ConsumptionResponseDto {
    @ApiModelProperty(value = "시퀀스", required = true)
    private long seq;

    @ApiModelProperty(value = "유저 아이디", required = true)
    private long userId;

    @ApiModelProperty(value = "유저 닉네임", required = true)
    private String nickname;

    @ApiModelProperty(value = "거주지", required = true, example = "강남구")
    private String area;

    @ApiModelProperty(value = "수도/가스/전기 타입", example = "GAS", required = true)
    private ConsumptionType type;

    @ApiModelProperty(value = "이번 달 소비량", example = "990", required = true)
    private int consumed;

    @ApiModelProperty(value = "소비 절약 랭킹", example = "1", required = true)
    private int rank;

    @ApiModelProperty(value = "지난 달 대비 소비 절약 랭킹", example = "1", required = false)
    private int rankAgainstPrevMonth;

    @ApiModelProperty(value = "현재 거주지 기준 총 가구 수", example = "30", required = true)
    private int totalHouseholds;

    @ApiModelProperty(value = "금일 소비량 데이터", required = true)
    private List<TodayConsumptionGraph> todayConsumedGraphData;

    @ApiModelProperty(value = "생성일", required = true)
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "수정일", required = true)
    private LocalDateTime updatedAt;

    @ApiModelProperty(value = "삭제 여부", required = true)
    private int status;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTodayConsumedGraphData(List<TodayConsumptionGraph> todayConsumedGraphData) {
        this.todayConsumedGraphData = todayConsumedGraphData;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setTotalHouseholds(int totalHouseholds) {
        this.totalHouseholds = totalHouseholds;
    }

    public void setRankAgainstPrevMonth(int rankAgainstPrevMonth) {
        this.rankAgainstPrevMonth = rankAgainstPrevMonth;
    }

    @Builder
    public ConsumptionResponseDto(Consumption entity) {
        this.seq = entity.getSeq();
        this.userId = entity.getUserId();
        this.area = entity.getArea();
        this.type = entity.getType();
        this.consumed = entity.getConsumed();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
        this.status = entity.getStatus() == 0 ? 1 : entity.getStatus();
    }

    //    소비량 그래프 이너 클래스
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Builder
    static public class TodayConsumptionGraph {
        private LocalDateTime createdAt;
        private int consumed;
    }
}