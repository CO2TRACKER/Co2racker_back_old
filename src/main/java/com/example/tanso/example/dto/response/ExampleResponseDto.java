package com.example.tanso.example.dto.response;

import com.example.tanso.example.domain.model.Example;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@ApiModel(description = "예제 응답 DTO")
@Getter
@NoArgsConstructor
public class ExampleResponseDto {
    @ApiModelProperty(value = "시퀀스")
    private long seq;

    @ApiModelProperty(value = "제목")
    private String title;

    @ApiModelProperty(value = "내용")
    private String content;

    @ApiModelProperty(value = "생성일")
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "수정일")
    private LocalDateTime updatedAt;

    @Builder
    public ExampleResponseDto(Example entity) {
        this.seq = entity.getSeq();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
