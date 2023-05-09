package com.example.tanso.example.dto.request;

import com.example.tanso.example.domain.model.Example;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(description = "연동용 DTO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExampleSaveRequestDto {
    @ApiModelProperty(value = "제목", required = true, example = "example title")
    private String title;

    @ApiModelProperty(value = "내용", required = true, example = "example content")
    private String content;

    public Example toEntity() {
        return Example.builder()
                .title(title)
                .content(content)
                .build();
    }
}
