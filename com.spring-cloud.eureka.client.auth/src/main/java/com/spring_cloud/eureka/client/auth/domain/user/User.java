package com.spring_cloud.eureka.client.auth.domain.user;

import com.spring_cloud.eureka.client.auth.domain.common.BaseEntity;
import com.spring_cloud.eureka.client.auth.domain.deliveryManager.DeliveryManager;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "p_users")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class User extends BaseEntity {

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


    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    // 비밀번호 설정 메서드 추가
    public void setPassword(String password) {
        this.password = password;
    }

}