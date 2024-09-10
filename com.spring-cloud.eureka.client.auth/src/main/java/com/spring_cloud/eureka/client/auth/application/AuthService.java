package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.application.dto.AuthResponseDto;
import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.domain.User;
import com.spring_cloud.eureka.client.auth.domain.UserRepository;
import com.spring_cloud.eureka.client.auth.domain.UserRoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;

    // MANAGER_TOKEN
    private final String DELIVERY_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    // MANAGER_TOKEN
    private final String HUB_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    //MASTER_TOKEN
    private final String MASTER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 생성자 주입
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, @Value("${service.jwt.secret-key}") String secretKey) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
    }


    // 토큰 생성
    public AuthResponseDto createAccessToken(final String username) {
        return userRepository.findByUsername(username)
                .map(user -> AuthResponseDto.of(
                        Jwts.builder()
                                .claim("username", user.getUsername())
                                .claim("role", user.getRole()) // 역할 추가
                                .issuer(issuer)
                                .issuedAt(new Date(System.currentTimeMillis()))
                                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                                .signWith(secretKey, SignatureAlgorithm.HS512)
                                .compact())
                ).orElseThrow();
    }

    // 회원가입
    @Transactional
    public void signUp(SignUpRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // 사용자 ROLE 확인
        boolean isDeliveryManager = false;
        boolean isHubManager = false;
        boolean isMaster = false;

        // 기본고객
        UserRoleEnum role = UserRoleEnum.COMPANY;

        // 배송관리자
        if (requestDto.isDeliveryManager()) {
            if (!DELIVERY_MANAGER_TOKEN.equals(requestDto.getDeliveryManagerToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isDeliveryManager = true;
            role = UserRoleEnum.DELIVERY_MANAGER;
        }

        // 허브관리자
        if (requestDto.isHubManager()) {
            if (!HUB_MANAGER_TOKEN.equals(requestDto.getHubManagerToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isHubManager = true;
            role = UserRoleEnum.HUB_MANAGER;
        }

        // 마스터
        if (requestDto.isMaster()) {
            if (!MASTER_TOKEN.equals(requestDto.getMasterToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isMaster = true;
            role = UserRoleEnum.MASTER;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
    }

    // 로그인
    @Transactional
    public boolean login(LoginRequestDto loginRequestDto) {
        Optional<User> optionalUser = userRepository.findByUsername(loginRequestDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword());
        }
        return false;
    }
}
