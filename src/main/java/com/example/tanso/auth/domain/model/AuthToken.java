package com.example.tanso.auth.domain.model;

import com.example.tanso.boot.domain.BaseEntity;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "tb_auth_token")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class AuthToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(255) comment 'Access Token'")
    private String accessToken;

    @Column(columnDefinition = "bigint comment '유저 시퀀스'")
    private Long userId;

    // 영속성 컨텍스트를 활용하여 Access Token 을 업데이트한다.
    public void updateAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

}
