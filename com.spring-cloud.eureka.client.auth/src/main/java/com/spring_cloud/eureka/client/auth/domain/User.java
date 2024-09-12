package com.spring_cloud.eureka.client.auth.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "p_users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToOne(mappedBy = "user")
    private DeliveryManager deliveryManager;

    @Builder.Default
    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;  // 논리적 삭제 플래그의 기본값 설정

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isDelete = false;  // 기본값 설정
    }
    // 비밀번호 설정 메서드 추가
    public void setPassword(String password) {
        this.password = password;
    }

    //소프트 딜리트
    public void userDeleted() {
        this.isDelete = true;
    }
}