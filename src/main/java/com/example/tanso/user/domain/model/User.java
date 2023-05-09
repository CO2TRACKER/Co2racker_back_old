package com.example.tanso.user.domain.model;

import com.example.tanso.boot.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "tb_user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "varchar(100) comment '회원 이메일'")
    private String username;

    @Column(columnDefinition = "varchar(255) comment '회원 비밀번호(암호화)'")
    private String password;

    @Column(columnDefinition = "varchar(100) comment '회원 닉네임'")
    private String nickname;

//    @Column(columnDefinition = "varchar(100) comment '전기 고객 번호'")
//    private String elecCustomerIdentifier;
//
//    @Column(columnDefinition = "varchar(100) comment '수도 고객 번호'")
//    private String waterCustomerIdentifier;

    @Column(columnDefinition = "varchar(100) comment '도로명 주소'")
    private String addr;

    @Column(columnDefinition = "varchar(100) comment '상세 주소'")
    private String detailAddr;

    @Column(columnDefinition = "bigint default '0' NOT NULL comment '유저 포인트'")
    private long point;
}
