package com.example.tanso.consume.domain.model;

import com.example.tanso.boot.domain.BaseEntity;
import com.example.tanso.consume.enums.ConsumptionType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "tb_comsumption")
@Getter
@NoArgsConstructor
public class Consumption extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long seq;

    @Column(columnDefinition = "bigint NOT NULL comment '유저 아이디'")
    private long userId;

    @Column(columnDefinition = "varchar(100) NOT NULL comment '거주지'")
    private String area;

    @Column(columnDefinition = "varchar(50) NOT NULL comment '수도/가스/전기 타입'")
    private ConsumptionType type;

    @Column(columnDefinition = "int NOT NULL comment '소비량'")
    private int consumed;

    @Builder
    public Consumption(
            long userId,
            String area,
            ConsumptionType type,
            int consumed
    ) {
        this.userId = userId;
        this.area = area;
        this.type = type;
        this.consumed = consumed;
    }
}
