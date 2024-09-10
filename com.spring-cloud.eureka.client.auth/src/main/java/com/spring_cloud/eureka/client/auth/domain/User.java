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

    // 유저 생성 메서드
//    public static User create(String username, String password, UserRoleEnum role) {
//        return User.builder()
//                .username(username)
//                .password(password)
//                .role(role)
//                .build();
//    }

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}