package com.example.tanso.example.domain.model;

import com.example.tanso.boot.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@ApiModel(description = "연동용 엔티티")
@Entity
@Table(name = "tb_example")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Example extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    private String title;
    private String content;
}
