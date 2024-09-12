package com.spring_cloud.eureka.client.auth.application;

import com.spring_cloud.eureka.client.auth.application.dto.LoginRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.SignUpRequestDto;
import com.spring_cloud.eureka.client.auth.application.dto.UserInfoResponseDto;
import com.spring_cloud.eureka.client.auth.application.dto.UserInfoUpdateRequestDto;
import com.spring_cloud.eureka.client.auth.application.security.JwtUtil;
import com.spring_cloud.eureka.client.auth.application.security.UserDetailsImpl;
import com.spring_cloud.eureka.client.auth.domain.User;
import com.spring_cloud.eureka.client.auth.domain.UserRepository;
import com.spring_cloud.eureka.client.auth.domain.UserRoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    // MANAGER_TOKEN
    private final String DELIVERY_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    // MANAGER_TOKEN
    private final String HUB_MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
    //MASTER_TOKEN
    private final String MASTER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 생성자 주입
    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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
    public String login(LoginRequestDto loginRequestDto) {
        try {
            // 사용자의 인증 정보 생성
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDto.getUsername(),
                            loginRequestDto.getPassword()
                    )
            );

            // 인증이 성공하면 인증된 사용자 정보 가져오기
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getUser().getUserId();
            String username = userDetails.getUsername();
            UserRoleEnum role = userDetails.getUser().getRole();

            // JWT 토큰 생성
            return jwtUtil.createToken(userId, username, role);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username or password");
        }
    }

    // 사용자 정보 수정
    @Transactional
    public void updateUserInfo(String username, UserInfoUpdateRequestDto requestDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 비밀번호 수정
        if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        }

        // 사용자 이름 수정은 불가능하므로 이름 변경은 제외됨

        userRepository.save(user);
    }

    // 사용자 삭제
    @Transactional
    public void softDeleteUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof String username) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.userDeleted();
            userRepository.save(user);
        } else {
            throw new RuntimeException("No authentication information found");
        }
    }

    // 현재 로그인한 사용자 정보 조회
    public UserInfoResponseDto getUserInfo(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        return new UserInfoResponseDto(user.getUsername(), user.getRole());
    }

    // 모든 사용자 정보 조회 (마스터 권한)
    public List<UserInfoResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();

        // 각 사용자의 정보를 UserInfoResponseDto로 변환하여 반환
        return users.stream()
                .map(user -> new UserInfoResponseDto(user.getUsername(), user.getRole()))
                .toList();
    }


}
