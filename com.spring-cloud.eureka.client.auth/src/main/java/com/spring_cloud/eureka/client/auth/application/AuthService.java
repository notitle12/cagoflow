package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.UserInfoResponseDto;
import com.spring_cloud.eureka.client.auth.domain.User;
import com.spring_cloud.eureka.client.auth.domain.UserRepository;
import com.spring_cloud.eureka.client.auth.domain.UserRoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // MANAGER_TOKEN
    private final String DELIVERY_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    // MANAGER_TOKEN
    private final String HUB_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    //MASTER_TOKEN
    private final String MASTER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 생성자 주입
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    // 현재 로그인한 사용자 정보 조회
    public UserInfoResponseDto getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserInfoResponseDto(user.getUsername(), user.getRole());
    }
}
